package me.jumen.springdatajpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = "all_post", query = "SELECT p FROM Post AS p")
})

@Entity
@Getter
@Setter
@ToString
public class Post extends AbstractAggregateRoot<Post> {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Lob
    private String content;

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

    public Post publish() {
        this.registerEvent(new PostPublishedEvent(this));
        return this;
    }
}
