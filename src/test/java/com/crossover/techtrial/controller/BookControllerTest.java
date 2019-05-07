package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.MemberRepository;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.crossover.techtrial.utils.Utilities.getHttpEntity;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author samir
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    private static final String BOOK_TITLE = "Title";

    private Book book;

    @Autowired
    private TestRestTemplate template;


    @Autowired
    BookRepository bookRepository;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setUp() {
        book = new Book();
        book.setTitle(BOOK_TITLE);
    }

    @Test
    public void getBooks() {
        //Act
        ResponseEntity<List<Book>> findByIdResponse = template.exchange(
                "/api/book/", HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {
                });

        //Assert
        errorCollector.checkThat(200, IsEqual.equalTo(findByIdResponse.getStatusCode().value()));
    }

    @Test
    public void getRideById() {
        //Arrange
        final ResponseEntity<Book> bookResponseEntity = createBook(book);

        //Act
        final ResponseEntity<Book> byId = findById(bookResponseEntity.getBody().getId());

        //Assert
        errorCollector.checkThat(200, equalTo(bookResponseEntity.getStatusCode().value()));
        errorCollector.checkThat(200, equalTo(byId.getStatusCode().value()));
        errorCollector.checkThat(BOOK_TITLE, equalTo(byId.getBody().getTitle()));

        //Cleanup
        bookRepository.deleteById(byId.getBody().getId());
    }

    private ResponseEntity<Book> findById(Long id) {
        return template.exchange(
                "/api/book/" + id, HttpMethod.GET, null, Book.class);
    }

    private ResponseEntity<Book> createBook(Book book) {
        return template.postForEntity(
                "/api/book", getHttpEntity(book), Book.class);
    }

}
