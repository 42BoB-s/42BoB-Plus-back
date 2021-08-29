package com.example.projectprototype.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ban")
@Getter @Setter
@NoArgsConstructor
public class Ban extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    private User src;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    private User dest;

    public void setSrc(User srcUser) {
        if (this.src != null) {
            this.src.getBanSrcList().remove(this);
        }
        this.src = srcUser;
        srcUser.getBanSrcList().add(this);
    }

    public void setDest(User destUser) {
        if (this.dest != null) {
            this.dest.getBanDestList().remove(this);
        }
        this.dest = destUser;
        destUser.getBanDestList().add(this);
    }
}
