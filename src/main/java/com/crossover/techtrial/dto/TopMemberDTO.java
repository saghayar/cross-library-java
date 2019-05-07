package com.crossover.techtrial.dto;

/**
 * @author crossover
 */
public class TopMemberDTO {

    private Long id;
    private String name;
    private String email;
    private Long bookCount;

    public TopMemberDTO(Long id, String name, String email, Long bookCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bookCount = bookCount;
    }

    public Long getId() {
        return id;
    }

    public TopMemberDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TopMemberDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public TopMemberDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public Long getBookCount() {
        return bookCount;
    }

    public TopMemberDTO setBookCount(Long bookCount) {
        this.bookCount = bookCount;
        return this;
    }
}

