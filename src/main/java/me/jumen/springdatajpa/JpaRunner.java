package me.jumen.springdatajpa;

import org.hibernate.Session;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/*
 * @Transactional - EntityManager 와 관련된 모든 행위들은 한 트랜잭션 안에서 일어나야 한다.
 * 1. 클래스에 붙이면 모든 메서드에 적용된다.
 * 2. 메서드에만 붙일 수도 있다.
 * */

@Component
@Transactional
public class JpaRunner implements ApplicationRunner {

    @PersistenceContext
    EntityManager entityManager;    // JPA 핵심 클래스 (SpringBoot의 ApplicationContext와 같은 위상)


    @Override
    public void run(ApplicationArguments args) throws Exception {

        /*
         * Transient 상태 : JPA가 아무것도 모르는 상태
         * */
        Study study = new Study();
        study.setName("Spring Data JPA");

        Account account = new Account();
        account.setUsername("WON");
        account.setPassword("hibernate");
        account.addStudy(study); /* 양방향 관계 설정하기 */

        //Post-Comment CASCADE 예제
        Post post = new Post();
        post.setTitle("Spring DATA JPA 포스트 타이틀");

        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Comment comment = new Comment();
            comment.setComment("Comment " + i);
            post.addComment(comment);
        }

        Session session = entityManager.unwrap(Session.class);  /* 하이버네이트의 가장 핵심적인 API는 Session이다.*/
//        session.save(post);
        post = session.get(Post.class, 1l);
        System.out.println("==========Post는 Fetch.LAZY이기 때문에 comments까지 바로 가져오지 않는다.");
        System.out.println(post.getTitle());

        post.getComments().forEach(c -> {
            System.out.println(c.getComment());
        });

        Comment comment = session.get(Comment.class, 5l);
        System.out.println("========== Comment는 Fetch.EAGER이기 때문에 query는 한번만 날아가서 post까지 가져온다");
        System.out.println(comment.getComment());





        /*  Account-Study 예제 */

//
//        /*
//         * Persistent 상태 : JPA가 관리중인 상 (1차 캐시)
//         * save를 호출한다고 해서 바로 INSERT 쿼리가 발생하는 것은 아니다.
//         * save를 호출하면 1차 캐시 : PersistenceContext에 이 instance를 넣어서 캐싱한다.
//         * */
//
////      entityManager.persist(account);   //JPA를 통한 save 방법
//        session.save(account);
//        session.save(study);
//
//        Account load = session.load(Account.class, account.getId());
//        load.setUsername("WON 1");
//        load.setUsername("WON 2");
//        load.setUsername("WON");
//
//
//        /*
//         * 이렇게 했는데도 update 쿼리가 실행되지 않는다.
//         * 1. Dirty Checking (이 객체의 변경사항을 감시한다)
//         * 2. Write Behind (객체의 변경사항을 최대한 늦은 시점에 반영한다.)
//         *
//         * 계속 감시하다 보니까 변경사항이 처음이랑 똑같다.(WON -> WON 1 -> WON 2 -> WON)
//         * update 하지 않겠다.
//         * */
//
//        System.out.println("========== 현재 상태에서는 캐싱된 값을 보여주므로 이게 끝나고 INSERT가 동작한다. ==========");
//        System.out.println("========== 실제 INSERT는 트랜잭션이 끝날때 발생한다. ==========");
//        System.out.println(load.toString());

    }
}
