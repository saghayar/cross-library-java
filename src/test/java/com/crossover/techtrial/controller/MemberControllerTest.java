/*
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.dto.TopMemberDTO;
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
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.crossover.techtrial.model.MembershipStatus.ACTIVE;
import static com.crossover.techtrial.utils.Utilities.dummyString;
import static com.crossover.techtrial.utils.Utilities.getHttpEntity;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author kshah
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    private static final String MEMBER_EMAIL = "member555@gmail.com";
    private static final String MEMBER_NAME = "Member name";
    private static final Long MEMBER_ID = 222L;

    private Member member;
    private MockMvc mockMvc;

    @Mock
    private MemberController memberController;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        member = new Member();
        member.setId(MEMBER_ID);
        member.setEmail(dummyString().concat(MEMBER_EMAIL));
        member.setName(dummyString().concat(MEMBER_NAME));
        member.setMembershipStatus(ACTIVE);
        member.setMembershipStartDate(LocalDateTime.now());
    }

    @Test
    public void registerMember() {
        //Act
        ResponseEntity<Member> response = createMember(member);

        //Assert
        errorCollector.checkThat(member.getName(), equalTo(response.getBody().getName()));
        errorCollector.checkThat(member.getEmail(), equalTo(response.getBody().getEmail()));
        errorCollector.checkThat(member.getMembershipStatus(), equalTo(response.getBody().getMembershipStatus()));
        errorCollector.checkThat(200, equalTo(response.getStatusCode().value()));

        //Cleanup
        memberRepository.deleteById(response.getBody().getId());
    }

    @Test
    public void whenMemberNameLengthIsLessThanTwoThenReturnBadRequest() {
        //Arrange
        member.setName("A");

        //Act
        ResponseEntity<Member> response = createMember(member);

        //Assert
        errorCollector.checkThat(400, equalTo(response.getStatusCode().value()));
    }

    @Test
    public void whenMemberNameLengthStartWithDigitThanTwoThenReturnBadRequest() {
        //Arrange
        member.setName("2");

        //Act
        ResponseEntity<Member> response = createMember(member);

        //Assert
        errorCollector.checkThat(400, equalTo(response.getStatusCode().value()));
    }

    @Test
    public void getMemberById() {
        ResponseEntity<Member> createResponse = createMember(member);
        final Member createResponseBody = createResponse.getBody();

        ResponseEntity<Member> findByIdResponse = findById(createResponseBody.getId());
        final Member findByIdResponseBody = findByIdResponse.getBody();

        errorCollector.checkThat(200, equalTo(createResponse.getStatusCode().value()));
        errorCollector.checkThat(200, equalTo(findByIdResponse.getStatusCode().value()));
        errorCollector.checkThat(findByIdResponseBody.getName(), equalTo(createResponseBody.getName()));
        errorCollector.checkThat(findByIdResponseBody.getEmail(), equalTo(createResponseBody.getEmail()));
        errorCollector.checkThat(findByIdResponseBody.getMembershipStatus(), equalTo(createResponseBody.getMembershipStatus()));

        //Cleanup
        memberRepository.deleteById(findByIdResponseBody.getId());
    }

    @Test
    public void whenGetMemberByIdThenReturnMemberNotFound() {
        //Act
        ResponseEntity<Member> findByIdResponse = findById(44441L);

        //Assert
        errorCollector.checkThat(404, equalTo(findByIdResponse.getStatusCode().value()));
    }

    @Test
    public void allMembers() {
        //Act
        ResponseEntity<List<Member>> findByIdResponse = template.exchange(
                "/api/member/", HttpMethod.GET, null, new ParameterizedTypeReference<List<Member>>() {
                });

        //Assert
        errorCollector.checkThat(200, equalTo(findByIdResponse.getStatusCode().value()));
    }

    private ResponseEntity<Transaction> createTransaction(Book book, Member member) {
        Map<String, Long> params = new HashMap<>();
        params.put("bookId", book.getId());
        params.put("memberId", member.getId());

        return template.postForEntity("/api/transaction",
                getHttpEntity(params), Transaction.class);
    }

    private ResponseEntity<Member> findById(Long id) {
        return template.exchange(
                "/api/member/" + id, HttpMethod.GET, null, Member.class);
    }

    private ResponseEntity<Member> createMember(Member member) {
        return template.postForEntity(
                "/api/member", getHttpEntity(member), Member.class);
    }

    private ResponseEntity<Book> createBook(Book book) {
        return template.postForEntity(
                "/api/book", getHttpEntity(book), Book.class);
    }

}
