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
import io.swagger.annotations.*;

@SwaggerDefinition
@Path("/books")
@Api("Books")
public class BookEndpoint {

    @Inject
    private BookRepository bookRepository;

    @GET
    @Path("/{id : \\d+}")
    @Produces(APPLICATION_JSON)
    @ApiOperation(value = "Returns a book given an identifier", response = Book.class)
    @ApiResponses({
            @ApiResponse(code = 200, message="Book found"),
            @ApiResponse(code = 400, message="Invalid book"),
            @ApiResponse(code = 404, message="Book not found")
    })
    public Response getBook(@PathParam("id") @Min(1) Long id) {
        Book book = bookRepository.find(id);
        if (book != null)
            return Response.ok(book).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Produces(APPLICATION_JSON)
    @ApiOperation(value = "Creates a book given a JSON Book representation", response = Book.class)
    @ApiResponses({
            @ApiResponse(code = 201, message="Book created"),
            @ApiResponse(code = 415, message="Format Exception"),
    })
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        book = bookRepository.create(book);
        URI createdURI = uriInfo.getBaseUriBuilder().path(book.getId().toString()).build();
        return Response.created(createdURI).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    @ApiOperation(value = "Deletes a book given an id", response = Book.class)
    @ApiResponses({
            @ApiResponse(code = 204, message="Book deleted"),
            @ApiResponse(code = 400, message="Invalid book"),
            @ApiResponse(code = 500, message="Book not found")
    })
    public Response deleteBook(@PathParam("id") @Min(1) Long id) {
        bookRepository.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @ApiOperation(value = "Returns all books in database", response = Book.class)
    @ApiResponses({
            @ApiResponse(code = 200, message="Books found"),
            @ApiResponse(code = 204, message="No books")
    })
    public Response getBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.size() != 0)
            return Response.ok(books).build();
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Produces(APPLICATION_JSON)
    @ApiOperation(value = "Returns the total number of books ", response = Book.class)
    @ApiResponses({
            @ApiResponse(code = 200, message="Number returned"),
            @ApiResponse(code = 204, message="No books")
    })
    public Response countBooks() {
        Long nbOfBooks = bookRepository.countAll();
        if (nbOfBooks != 0)
            return Response.ok(nbOfBooks).build();
        return Response.noContent().build();
    }


}
