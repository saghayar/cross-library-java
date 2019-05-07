package com.crossover.techtrial.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author kshah
 */
@Entity
@Table(name = "member")
public class Member implements Serializable {

    private static final long serialVersionUID = 9045098179799205444L;

    @Column(name = "membership_start_date")
    private LocalDateTime membershipStartDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(min = 2, max = 100)
    @Pattern(regexp = "[a-zA-Z].*")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public LocalDateTime getMembershipStartDate() {
        return membershipStartDate;
    }

    public void setMembershipStartDate(LocalDateTime membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (!id.equals(member.id)) return false;
        if (!name.equals(member.name)) return false;
        if (!email.equals(member.email)) return false;
        if (membershipStatus != member.membershipStatus) return false;
        return membershipStartDate.equals(member.membershipStartDate);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        int prime = 31;
        result = prime * result + name.hashCode();
        result = prime * result + email.hashCode();
        result = prime * result + membershipStatus.hashCode();
        result = prime * result + membershipStartDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Member [id=" + id + ", name=" + name + ", email=" + email + "]";
    }


}
