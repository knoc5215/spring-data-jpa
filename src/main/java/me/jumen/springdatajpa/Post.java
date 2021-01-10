package me.jumen.springdatajpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    /*
    * CascadeType.ALL 전파하라
    * OneToMany에서 fetch default = LAZY
    * */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    public void addComment(Comment comment) {
        this.getComments().add(comment);
        comment.setPost(this);
    }
}
