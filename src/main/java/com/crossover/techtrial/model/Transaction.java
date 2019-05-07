package com.crossover.techtrial.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author kshah
 */
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8951221480021840448L;
    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    public Member member;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;
    //Date and time of issuance of this book
    @Column(name = "date_of_issue")
    private LocalDateTime dateOfIssue;

    //Date and time of return of this book
    @Column(name = "date_of_return")
    private LocalDateTime dateOfReturn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDateTime getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(LocalDateTime dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public LocalDateTime getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(LocalDateTime dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    @PreUpdate
    protected void onUpdate() {
        dateOfReturn = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", book=" + book + ", member=" + member + ", dateOfIssue=" + dateOfIssue + ", dateOfReturn=" + dateOfReturn + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!id.equals(that.id)) return false;
        if (!book.equals(that.book)) return false;
        if (!member.equals(that.member)) return false;
        if (!dateOfIssue.equals(that.dateOfIssue)) return false;
        return dateOfReturn != null ? dateOfReturn.equals(that.dateOfReturn) : that.dateOfReturn == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        int prime = 31;
        result = prime * result + book.hashCode();
        result = prime * result + member.hashCode();
        result = prime * result + dateOfIssue.hashCode();
        result = prime * result + (dateOfReturn != null ? dateOfReturn.hashCode() : 0);
        return result;
    }
}
