/*
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.exceptions.MemberNotFoundException;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author crossover
 */
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
