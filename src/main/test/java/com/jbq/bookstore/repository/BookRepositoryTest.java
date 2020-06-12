package com.jbq.bookstore.repository;

import com.jbq.bookstore.model.Book;
import com.jbq.bookstore.model.Language;
import junit.framework.TestCase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.rmi.server.UID;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BookRepositoryTest {

    @Inject
    private BookRepository bookRepository;

    @Test
    public void create() throws Exception {
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0, bookRepository.findAll().size());

        // Create a book
        Book book = new Book(new UID(), "A Title", 12f, 123,
                Language.ENGLISH, new Date(), "http://www.blahlah.com", "description");
        book = bookRepository.create(book);

        // Check created book by id
        assertNotNull(book.getId());

        // Find created book
        Book bookFound = bookRepository.find(book.getId());

        // Check the found book
        assertEquals("A Title", bookFound.getTitle());

        // Assert a book has been persisted
        assertEquals(Long.valueOf(1), bookRepository.countAll());
        assertEquals(1, bookRepository.findAll().size());

        System.out.println(bookFound.toString());

        // Delete a book
        bookRepository.delete(book.getId());


    }

    @Test
    public void delete(){
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0, bookRepository.findAll().size());

        // Create a book
        Book book = new Book(new UID(), "A Title", 12f, 123,
                Language.ENGLISH, new Date(), "http://www.blahlah.com", "description");
        book = bookRepository.create(book);

        System.out.println(book.toString());

        // Delete a book
        bookRepository.delete(book.getId());

        // Assert a book has been persisted
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0, bookRepository.findAll().size());

    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(BookRepository.class)
                .addClass(Book.class)
                .addClass(Language.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");

    }
}
