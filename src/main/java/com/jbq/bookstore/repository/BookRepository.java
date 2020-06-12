package com.jbq.bookstore.repository;

import com.jbq.bookstore.model.Book;
import com.jbq.bookstore.util.NumberGenerator;
import com.jbq.bookstore.util.TextUtil;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

@Transactional(Transactional.TxType.SUPPORTS)
public class BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private TextUtil textUtil;

    @Inject
    private NumberGenerator numberGenerator;

    public Book find(@NotNull Long id){
        return em.find(Book.class, id);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Book create(@NotNull Book book){
        book.setTitle(textUtil.sanitize(book.getTitle()));
        book.setIsbn(numberGenerator.generateNumber());
        em.persist(book);
        return book;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(@NotNull Long id){
        em.remove(em.getReference(Book.class, id));
    }

    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery(
                "SELECT b FROM Book b ORDER BY b.title DESC", Book.class);
        return query.getResultList();
    }

    public Long countAll(){
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(b) FROM Book b", Long.class);
        return query.getSingleResult();
    }


}
