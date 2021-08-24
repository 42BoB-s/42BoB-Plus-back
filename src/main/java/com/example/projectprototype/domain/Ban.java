package com.example.projectprototype.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "banlist")
@Getter @Setter
public class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "banuserid")
    private User src;

    @Column(name = "baneduserid")
    private String dest;

    public void setSrc(User user) {
        if (this.src != null) {
            this.src.getBanList().remove(this);
        }
        this.src = user;
        user.getBanList().add(this);
    }
}
