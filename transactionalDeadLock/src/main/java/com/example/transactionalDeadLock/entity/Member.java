package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String username;
    private String password;

    public Member(String email, String username, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}

