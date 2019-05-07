package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.repositories.TransactionRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static com.crossover.techtrial.model.MembershipStatus.ACTIVE;
import static com.crossover.techtrial.utils.Utilities.dummyString;
import static com.crossover.techtrial.utils.Utilities.getHttpEntity;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author samir
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

    private static final String BOOK_TITLE = "BookTitle";
    private static final String MEMBER_EMAIL = "member.email@gmail.com";
    private static final String MEMBER_NAME = "MemberName";
    private static final int SUCCESS = 200;

    private Book book;
    private Member member;
    private Transaction transaction;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BookRepository bookRepository;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setUp() {
        template.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        book = new Book();
        book.setTitle(BOOK_TITLE);

        member = new Member();
        member.setEmail(dummyString().concat(MEMBER_EMAIL));
        member.setName(dummyString().concat(MEMBER_NAME));
        member.setMembershipStatus(ACTIVE);

        transaction = new Transaction();
        transaction.setBook(book);
        transaction.setMember(member);
    }

    @Test
    public void issueBookToMember() {
        //Arrange
        ResponseEntity<Member> memberResponseEntity = createMember(member);
        ResponseEntity<Book> bookResponseEntity = createBook(book);
        final Member memberRs = memberResponseEntity.getBody();
        final Book bookRs = bookResponseEntity.getBody();

        Map<String, Long> params = new HashMap<>();
        params.put("bookId", bookRs.getId());
        params.put("memberId", memberRs.getId());

        //Act
        final ResponseEntity<Transaction> transactionResponseEntity = template.postForEntity("/api/transaction",
                getHttpEntity(params), Transaction.class);
        final Transaction transactionRs = transactionResponseEntity.getBody();

        //Assert
        errorCollector.checkThat(SUCCESS, equalTo(memberResponseEntity.getStatusCode().value()));
        errorCollector.checkThat(SUCCESS, equalTo(bookResponseEntity.getStatusCode().value()));
        errorCollector.checkThat(SUCCESS, equalTo(transactionResponseEntity.getStatusCode().value()));
        errorCollector.checkThat(transactionRs.getBook().getTitle(), equalTo(book.getTitle()));
        errorCollector.checkThat(transactionRs.getMember().getEmail(), equalTo(member.getEmail()));
        errorCollector.checkThat(transactionRs.getMember().getName(), equalTo(member.getName()));

        //Cleanup
        transactionRepository.deleteById(transactionRs.getId());
        memberRepository.deleteById(memberRs.getId());
        bookRepository.deleteById(bookRs.getId());
    }

    @Test
    public void whenIssueMoreThanFiveBooksToMemberThenReturnForbidden() {
        //Arrange
        List<Book> bookList = new ArrayList<>();
        List<Transaction> transactionList = new ArrayList<>();

        ResponseEntity<Member> memberResponseEntity = createMember(member);
        final Member memberRs = memberResponseEntity.getBody();

        for (int i = 1; i <= 10; i++) {
            ResponseEntity<Book> bookResponseEntity = createBook(book);
            Book bookRs = bookResponseEntity.getBody();
            bookList.add(bookRs);

            Map<String, Long> params = new HashMap<>();
            params.put("bookId", bookRs.getId());
            params.put("memberId", memberRs.getId());

            //Act
            ResponseEntity<Transaction> transactionResponseEntity = template.postForEntity("/api/transaction",
                    getHttpEntity(params), Transaction.class);
            Transaction transactionRs = transactionResponseEntity.getBody();
            transactionList.add(transactionRs);

            //Assert
            if (i <= 5) {
                errorCollector.checkThat(SUCCESS, equalTo(transactionResponseEntity.getStatusCode().value()));
                errorCollector.checkThat(transactionRs.getBook().getTitle(), equalTo(book.getTitle()));
                errorCollector.checkThat(transactionRs.getMember().getEmail(), equalTo(member.getEmail()));
                errorCollector.checkThat(transactionRs.getMember().getName(), equalTo(member.getName()));
            } else {
                errorCollector.checkThat(403, equalTo(transactionResponseEntity.getStatusCode().value()));
            }
        }

        //Cleanup
        transactionList.stream().filter(Objects::nonNull).forEach(t -> transactionRepository.deleteById(t.getId()));
        bookList.stream().filter(Objects::nonNull).forEach(b -> bookRepository.deleteById(b.getId()));
        memberRepository.deleteById(memberRs.getId());
    }

    @Test
    public void returnBookTransaction() {
        //Arrange
        ResponseEntity<Member> memberResponseEntity = createMember(member);
        ResponseEntity<Book> bookResponseEntity = createBook(book);
        final Member memberRs = memberResponseEntity.getBody();
        final Book bookRs = bookResponseEntity.getBody();

        //Act
        final ResponseEntity<Transaction> makeTransaction = createTransaction(bookRs, memberRs);

        final Long transactionId = makeTransaction.getBody().getId();
        final Map<String, Long> params = new HashMap<>();
        params.put("transaction-id", transactionId);

        final Transaction transaction = template.patchForObject("/api/transaction/" + transactionId + "/return",
                getHttpEntity(params), Transaction.class);

        //Assert
        errorCollector.checkThat(SUCCESS, equalTo(memberResponseEntity.getStatusCode().value()));
        errorCollector.checkThat(SUCCESS, equalTo(bookResponseEntity.getStatusCode().value()));
        errorCollector.checkThat(SUCCESS, equalTo(makeTransaction.getStatusCode().value()));

        errorCollector.checkThat(transaction.getBook().getTitle(), equalTo(book.getTitle()));
        errorCollector.checkThat(transaction.getMember().getEmail(), equalTo(member.getEmail()));
        errorCollector.checkThat(transaction.getMember().getName(), equalTo(member.getName()));

        //Cleanup
        transactionRepository.deleteById(transactionId);
        memberRepository.deleteById(memberRs.getId());
        bookRepository.deleteById(bookRs.getId());
    }

    private ResponseEntity<Member> createMember(Member member) {
        return template.postForEntity(
                "/api/member", getHttpEntity(member), Member.class);
    }

    private ResponseEntity<Book> createBook(Book book) {
        return template.postForEntity(
                "/api/book", getHttpEntity(book), Book.class);
    }

    private ResponseEntity<Transaction> createTransaction(Book book, Member member) {
        Map<String, Long> params = new HashMap<>();
        params.put("bookId", book.getId());
        params.put("memberId", member.getId());

        return template.postForEntity("/api/transaction",
                getHttpEntity(params), Transaction.class);
    }
}