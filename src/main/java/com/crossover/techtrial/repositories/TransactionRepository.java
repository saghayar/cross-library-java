package com.crossover.techtrial.repositories;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author crossover
 */
@RestResource(exported = false)
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Optional<Transaction> findFirstByBookAndMemberNot(Book book, Member member);

    List<Transaction> findTransactionsByMember(Member member);

    @Query("SELECT new com.crossover.techtrial.dto.TopMemberDTO(t.member.id, t.member.name, t.member.email, count(t.member) AS bookCount) FROM Transaction t " +
            "WHERE t.dateOfIssue BETWEEN ?1 AND ?2 AND t.dateOfReturn BETWEEN ?1 AND ?2 GROUP BY t.member " +
            "ORDER BY bookCount DESC")
    List<TopMemberDTO> findMemberTransactionsByDuration(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
