package me.jumen.springdatajpa;

import lombok.Getter;
import lombok.Setter;

import javax.inject.Named;
import javax.persistence.*;
import java.util.Date;

@NamedEntityGraph(name = "Comment.post", attributeNodes = @NamedAttributeNode("post"))
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private Date created;

    private Integer likeCount = 0;

    private int up;

    private int down;

    private boolean best;


}
