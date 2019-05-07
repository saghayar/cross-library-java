package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BookController for Book related APIs.
 *
 * @author crossover
 */
@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @GetMapping(path = "/api/book")
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookService.getAll());
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @PostMapping(path = "/api/book")
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.save(book));
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @GetMapping(path = "/api/book/{book-id}")
    public ResponseEntity<Book> getRideById(@PathVariable(name = "book-id") Long bookId) {
        Book book = bookService.findById(bookId);
        if (book != null)
            return ResponseEntity.ok(book);
        return ResponseEntity.notFound().build();
    }


}
