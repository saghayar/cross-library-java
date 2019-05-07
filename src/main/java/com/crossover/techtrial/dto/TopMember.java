package com.crossover.techtrial.dto;

import com.crossover.techtrial.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "topMember", types = {Transaction.class})
public interface TopMember {

    @Value("#{target.member.id}")
    Long getMemberId();

    @Value("#{target.member.name}")
    String getMemberName();

    @Value("#{target.member.email}")
    String getMemberEmail();

    @Value("#{target.bookCount}")
    Integer getBookCount();
}