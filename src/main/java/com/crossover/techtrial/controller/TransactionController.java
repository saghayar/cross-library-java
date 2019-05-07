/*
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.service.BookService;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @author kshah
 */
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    private final BookService bookService;

    private final MemberService memberService;

    @Autowired
    public TransactionController(TransactionService transactionService, BookService bookService, MemberService memberService) {
        this.transactionService = transactionService;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     * Example Post Request :  { "bookId":1,"memberId":33 }
     */
    @PostMapping(path = "/api/transaction")
    public ResponseEntity<Transaction> issueBookToMember(@RequestBody Map<String, Long> params) {

        Long bookId = params.get("bookId");
        Long memberId = params.get("memberId");

        final Book book = bookService.findById(bookId);
        final Member member = memberService.findById(memberId);

        if (transactionService.checkIfBookIssuedToSomeoneElse(book, member) ||
                transactionService.checkIfMemberHasMoreThanFiveBooks(member))
            return ResponseEntity.status(FORBIDDEN).build(); //Note2 && Task4

        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setMember(member);
        transaction.setDateOfIssue(LocalDateTime.now());

        return ResponseEntity.ok().body(transactionService.save(transaction));
    }

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @PatchMapping(path = "/api/transaction/{transaction-id}/return")
    public ResponseEntity<Transaction> returnBookTransaction(@PathVariable(name = "transaction-id") Long transactionId) {
        Transaction transaction = transactionService.findById(transactionId);

        if (transaction.getDateOfReturn() != null)
            return ResponseEntity.status(FORBIDDEN).build(); //Note4

        return ResponseEntity.ok().body(transactionService.save(transaction));
    }

}
