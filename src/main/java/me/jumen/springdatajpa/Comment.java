package me.jumen.springdatajpa;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@NamedEntityGraph(name = "Comment.post", attributeNodes = @NamedAttributeNode("post"))
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    private Date created;

    @CreatedBy
    @ManyToOne
    private Account createdBy;

    @LastModifiedDate
    private Date updated;

    @LastModifiedBy
    @ManyToOne
    private Account updatedBy;


    private Integer likeCount = 0;

    private int up;

    private int down;

    private boolean best;

    @PrePersist
    public void prePersist() {
        System.out.println("PrePersist is called");
        this.created = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println("PreUpdate is called");
        this.updated = new Date();
    }


}
