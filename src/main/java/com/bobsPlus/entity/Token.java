package com.bobsPlus.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token extends TimeEntity {
    @Id
    @Column
    private Long id;

    @Column(length = 4096)
    private String accessToken;

    @Column(length = 4096)
    private String refreshToken;
}