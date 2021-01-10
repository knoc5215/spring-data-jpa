package me.jumen.springdatajpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    private String comment;

    /*
     * ManyToOne에서 fetch default = EAGER
     * */
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;


}