package com.jbq.bookstore.repository;

import com.jbq.bookstore.model.Book;

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


    public Book find(@NotNull Long id){
        return em.find(Book.class, id);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Book create(@NotNull Book book){
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
