package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.exceptions.TransactionNotFoundException;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.*;

import static com.crossover.techtrial.model.MembershipStatus.ACTIVE;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author samir
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionServiceImplTest {

    private static final String BOOK_TITLE = "Technology";
    private static final String MEMBER_EMAIL = "test@gmail.com";
    private static final String MEMBER_EMAIL1 = "test1@gmail.com";
    private static final String MEMBER_NAME = "John";
    private static final String MEMBER_NAME1 = "Jane";
    private static final Long MEMBER_ID = 1L;
    private static final Long BOOK_COUNT = 3L;

    private Optional<Transaction> optionalTransaction;
    private TopMemberDTO topMemberDTO;
    private Book book;
    private Member member;
    private Transaction transaction;

    @Autowired
    TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @Captor
    private ArgumentCaptor<LocalDateTime> dateTimeArgumentCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setUp() {
        book = new Book();
        book.setTitle(BOOK_TITLE);

        member = new Member();
        member.setId(MEMBER_ID);
        member.setEmail(MEMBER_EMAIL);
        member.setName(MEMBER_NAME);
        member.setMembershipStatus(ACTIVE);

        transaction = new Transaction();
        transaction.setBook(book);
        transaction.setMember(member);

        topMemberDTO = new TopMemberDTO(MEMBER_ID, MEMBER_NAME, MEMBER_EMAIL, BOOK_COUNT);

        optionalTransaction = Optional.of(transaction);
    }

    @Test
    public void whenFindByIDThenReturnTransaction() {
        //Arrange
        when(transactionRepository.findById(anyLong())).thenReturn(optionalTransaction);

        //Act
        final Transaction byId = transactionService.findById(1L);

        //Assert
        errorCollector.checkThat(byId.getBook().getTitle(), equalTo(BOOK_TITLE));
        errorCollector.checkThat(byId.getMember().getEmail(), equalTo(MEMBER_EMAIL));
        errorCollector.checkThat(byId.getMember().getName(), equalTo(MEMBER_NAME));
        errorCollector.checkThat(byId.getMember().getMembershipStatus(), equalTo(ACTIVE));
    }

    @Test
    public void whenFindByIdThenThrowTransactionNotFoundException() {
        //Expectations
        expectedException.expect(TransactionNotFoundException.class);

        //Arrange
        when(transactionRepository.findById(anyLong())).thenThrow(TransactionNotFoundException.class);

        //Act
        transactionService.findById(1L);
    }


    @Test
    public void whenFindByDurationThenReturnTransactionList() {
        //Arrange
        final LocalDateTime now = LocalDateTime.now();
        Pageable pageable = new PageRequest(0, 5);
        when(transactionRepository.findMemberTransactionsByDuration(
                dateTimeArgumentCaptor.capture(),
                dateTimeArgumentCaptor.capture(),
                eq(pageable))
        ).thenReturn(Collections.singletonList(topMemberDTO));

        //Act
        final List<TopMemberDTO> byDuration = transactionService.findByDuration(now, now);

        //Assert
        errorCollector.checkThat(byDuration.size(), equalTo(1));
        errorCollector.checkThat(byDuration.get(0).getEmail(), equalTo(MEMBER_EMAIL));
        errorCollector.checkThat(byDuration.get(0).getName(), equalTo(MEMBER_NAME));
        errorCollector.checkThat(byDuration.get(0).getBookCount(), equalTo(BOOK_COUNT));
        errorCollector.checkThat(dateTimeArgumentCaptor.getAllValues(), equalTo(Arrays.asList(now, now)));
    }

    @Test
    public void checkIfBookIssuedToSomeoneElsePositiveCase() {
        //Arrange
        when(transactionRepository.findFirstByBookAndMemberNot(book, member))
                .thenReturn(optionalTransaction);

        //Act
        final boolean issuedToSomeoneElse = transactionService.checkIfBookIssuedToSomeoneElse(book, member);

        //Assert
        errorCollector.checkThat(issuedToSomeoneElse, equalTo(true));
    }

    @Test
    public void whenCheckIfBookIssuedToSomeoneElseNegativeCase() {
        //Arrange
        Member member = new Member();
        member.setId(MEMBER_ID + 1);
        member.setName(MEMBER_NAME1);
        member.setEmail(MEMBER_EMAIL1);

        Transaction trans = new Transaction();
        trans.setBook(book);
        trans.setMember(member);
        Optional<Transaction> optTransaction = Optional.of(trans);

        when(transactionRepository.findFirstByBookAndMemberNot(book, this.member))
                .thenReturn(optTransaction);

        //Act
        final boolean issuedToSomeoneElse = transactionService.checkIfBookIssuedToSomeoneElse(book, member);

        //Assert
        errorCollector.checkThat(issuedToSomeoneElse, equalTo(false));
    }

    @Test
    public void checkIfMemberHasMoreThanFiveBooksNegativeCase() {
        //Arrange
        List<Transaction> transactionList =new ArrayList<>();
        transactionList.add(transaction);
        when(transactionRepository.findTransactionsByMember(eq(member))).thenReturn(transactionList);

        //Act
        final boolean hasMoreThanFiveBooks = transactionService.checkIfMemberHasMoreThanFiveBooks(member);

        //Assert
        errorCollector.checkThat(hasMoreThanFiveBooks, equalTo(false));
    }

    @Test
    public void checkIfMemberHasMoreThanFiveBooksPositiveCase() {
        //Arrange
        when(transactionRepository.findTransactionsByMember(eq(member)))
                .thenReturn(Arrays.asList(transaction, transaction, transaction, transaction, transaction, transaction));

        //Act
        final boolean hasMoreThanFiveBooks = transactionService.checkIfMemberHasMoreThanFiveBooks(member);

        //Assert
        errorCollector.checkThat(hasMoreThanFiveBooks, equalTo(true));
    }

}