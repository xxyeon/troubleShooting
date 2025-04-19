package com.example.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    @Getter
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    private String title;

    @ColumnDefault("0")
    @Getter
    private int likes;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    public Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void updateLike(int like) {
        this.likes = like;
    }
}
