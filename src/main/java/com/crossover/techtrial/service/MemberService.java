/*
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Member;

import java.util.List;

/**
 * RideService for rides.
 *
 * @author crossover
 */
public interface MemberService {

    Member save(Member member);

    Member findById(Long memberId);

    List<Member> findAll();

}
