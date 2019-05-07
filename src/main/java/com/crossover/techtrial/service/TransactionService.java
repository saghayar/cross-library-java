package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);

    Transaction findById(Long id);

    boolean checkIfMemberHasMoreThanFiveBooks(Member member);

    List<TopMemberDTO> findByDuration(LocalDateTime from, LocalDateTime to);

    boolean checkIfBookIssuedToSomeoneElse(Book book, Member member);

}
