package com.jbq.bookstore.rest;

import static javax.ws.rs.core.MediaType.*;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import com.jbq.bookstore.model.Book;
import com.jbq.bookstore.repository.BookRepository;

@Path("/books")
public class BookEndpoint {

    @Inject
    private BookRepository bookRepository;

    @GET
    @Path("/{id : \\d+}")
    @Produces(APPLICATION_JSON)
    public Response getBook(@PathParam("id") @Min(1) Long id) {
        Book book = bookRepository.find(id);
        if (book != null)
            return Response.ok(book).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        book = bookRepository.create(book);
        URI createdURI = uriInfo.getBaseUriBuilder().path(book.getId().toString()).build();
        return Response.created(createdURI).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    public Response deleteBook(@PathParam("id") @Min(1) Long id) {
        bookRepository.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.size() != 0)
            return Response.ok(books).build();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Produces(APPLICATION_JSON)
    public Response countBooks() {
        Long nbOfBooks = bookRepository.countAll();
        if (nbOfBooks != 0)
            return Response.ok(nbOfBooks).build();
        return Response.noContent().build();
    }


}
