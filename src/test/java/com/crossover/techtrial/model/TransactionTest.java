package com.crossover.techtrial.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

public class TransactionTest {

    Transaction transaction1;
    Transaction transaction2;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setUp() {
        Member member = new Member();
        member.setId(3L);
        member.setName("Name");
        member.setEmail("Email@gmail.com");
        member.setMembershipStartDate(LocalDateTime.now());
        member.setMembershipStatus(MembershipStatus.ACTIVE);

        Book book = new Book();
        book.setId(2L);
        book.setTitle("Title");
        LocalDateTime dateTime = LocalDateTime.now();

        transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setBook(book);
        transaction1.setMember(member);
        transaction1.setDateOfIssue(dateTime);
        transaction1.setDateOfReturn(dateTime);

        transaction2 = new Transaction();
        transaction2.setId(1L);
        transaction2.setMember(member);
        transaction2.setBook(book);
        transaction2.setDateOfIssue(dateTime);
        transaction2.setDateOfReturn(dateTime);
    }

    @Test
    public void equalsPositiveScenario() {
        errorCollector.checkThat(transaction1, equalTo(transaction2));
    }

    @Test
    public void equalsNegativeScenario() {
        transaction1.setDateOfReturn(LocalDateTime.now().plusDays(1));
        errorCollector.checkThat(transaction1, not(equalTo(transaction2)));
    }

    @Test
    public void hashcodePositiveScenario() {
        final int hashCode1 = transaction1.hashCode();
        final int hashCode2 = transaction2.hashCode();
        errorCollector.checkThat(hashCode1, equalTo(hashCode2));
    }

    @Test
    public void hashcodeNegativeScenario() {
        transaction1.setDateOfReturn(LocalDateTime.now().plusDays(1));
        final int hashCode1 = transaction1.hashCode();
        final int hashCode2 = transaction2.hashCode();
        errorCollector.checkThat(hashCode1, not(equalTo(hashCode2)));
    }
}