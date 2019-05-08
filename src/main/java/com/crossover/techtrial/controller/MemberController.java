package com.crossover.techtrial.controller;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author crossover
 */

@RestController
public class MemberController {

    private final MemberService memberService;

    private final TransactionService transactionService;

    @Autowired
    public MemberController(MemberService memberService, TransactionService transactionService) {
        this.memberService = memberService;
        this.transactionService = transactionService;
    }

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @PostMapping(path = "/api/member")
    public ResponseEntity<Member> register(@RequestBody Member p) {
        return ResponseEntity.ok(memberService.save(p));
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @GetMapping(path = "/api/member")
    public ResponseEntity<List<Member>> getAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @GetMapping(path = "/api/member/{member-id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Member> getMemberById(@PathVariable(name = "member-id") Long memberId) {
        Member member = memberService.findById(memberId);
        if (member != null) {
            return ResponseEntity.status(OK).body(member);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * This API returns the top 5 members who issued the most books within the search duration.
     * Only books that have dateOfIssue and dateOfReturn within the mentioned duration should be counted.
     * Any issued book where dateOfIssue or dateOfReturn is outside the search, should not be considered.
     * <p>
     * DONT CHANGE METHOD SIGNATURE AND RETURN TYPES
     *
     * @return top 5 transactions by member
     */
    @GetMapping(path = "/api/member/top-member")
    public ResponseEntity<List<TopMemberDTO>> getTopMembers(
            @RequestParam(value = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
            @RequestParam(value = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime
    ) {
        List<TopMemberDTO> topMembers = transactionService.findByDuration(startTime, endTime);
        return ResponseEntity.ok(topMembers);
    }

}
