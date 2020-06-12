package com.jbq.bookstore.repository;

import com.jbq.bookstore.model.Book;
import com.jbq.bookstore.model.Language;
import com.jbq.bookstore.util.IsbnGenerator;
import com.jbq.bookstore.util.NumberGenerator;
import com.jbq.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.rmi.server.UID;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BookRepositoryTest {

    @Inject
    private BookRepository bookRepository;

    private Date date = new Date();
    private Date date2 = new Date();


    @Test(expected = Exception.class)
    public void findWithInvalidId(){
        bookRepository.find(null);
    }

    @Test(expected = Exception.class)
    public void createInvalidBook(){

        Book book = new Book("isbn", null, 12f, 123,
                Language.ENGLISH, new Date(), "http://www.blahlah.com", "description");
        book = bookRepository.create(book);
    }

    @Test
    public void create() throws Exception {
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0, bookRepository.findAll().size());
        // Create a book
        Book book = new Book("isbn", "A  Title", 12f, 123,
                Language.ENGLISH, date, "http://www.blahlah.com", "description");
        book = bookRepository.create(book);

        // Check created book by id
        assertNotNull(book.getId());

        // Find created book
        Book bookFound = bookRepository.find(book.getId());

        // Check the found book
        assertEquals("A Title", bookFound.getTitle());
        assertTrue(bookFound.getIsbn().startsWith("13"));

        // Assert a book has been persisted
        assertEquals(Long.valueOf(1), bookRepository.countAll());
        assertEquals(1, bookRepository.findAll().size());

        // Delete a book
        bookRepository.delete(book.getId());


    }

    @Test
    public void delete(){
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0, bookRepository.findAll().size());

        // Create a book
        Book book = new Book("isbn", "A Title", 12f, 123,
                Language.ENGLISH, date2, "http://www.blahlah.com", "description");
        book = bookRepository.create(book);

        // Delete a book
        bookRepository.delete(book.getId());

    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(BookRepository.class)
                .addClass(Book.class)
                .addClass(Language.class)
                .addClass(TextUtil.class)
                .addClass(NumberGenerator.class)
                .addClass(IsbnGenerator.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");

    }
}
