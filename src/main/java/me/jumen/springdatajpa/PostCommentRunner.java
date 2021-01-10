package me.jumen.springdatajpa;

import org.hibernate.Session;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

//@Component
@Transactional
public class PostCommentRunner implements ApplicationRunner {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void run(ApplicationArguments args) {
        Post post = new Post();
        post.setTitle("Spring DATA JPA 포스트 타이틀");

        for (int i = 0; i < 10; i++) {
            Comment comment = new Comment();
            comment.setComment("Comment " + i);
            post.addComment(comment);
        }


        Session session = entityManager.unwrap(Session.class);  /* 하이버네이트의 가장 핵심적인 API는 Session이다.*/
        post = session.get(Post.class, 1l);
        System.out.println("==========Post는 Fetch.LAZY이기 때문에 comments까지 바로 가져오지 않는다.");
        System.out.println(post.getTitle());

        post.getComments().forEach(c -> {
            System.out.println(c.getComment());
        });

        Comment comment = session.get(Comment.class, 5l);
        System.out.println("========== Comment는 Fetch.EAGER이기 때문에 query는 한번만 날아가서 post까지 가져온다");
        System.out.println(comment.getComment());


    }
}
