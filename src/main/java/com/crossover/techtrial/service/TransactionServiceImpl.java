/*
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.exceptions.TransactionNotFoundException;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author crossover
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(TransactionNotFoundException::new);
    }

    @Override
    public List<TopMemberDTO> findByDuration(LocalDateTime from, LocalDateTime to) {
        return transactionRepository
                .findMemberTransactionsByDuration(from, to, new PageRequest(0, 5));
    }

    @Override
    public boolean checkIfBookIssuedToSomeoneElse(Book book, Member member) {
        final Optional<Transaction> optionalTransaction = transactionRepository.findFirstByBookAndMemberNot(book, member);
        return optionalTransaction.isPresent();
    }

    @Override
    public boolean checkIfMemberHasMoreThanFiveBooks(Member member) {
        final List<Transaction> transactionsByMember = transactionRepository.findTransactionsByMember(member);
        return transactionsByMember.size() >= 5;
    }
}
