package me.jumen.springdatajpa;

import org.hibernate.Session;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


@Component
@Transactional // entityManager과 관련된 모든 행위들은 한 트랜잭션 안에서 일어나야 한다. 클래스에 붙이면 모든 메서드에 적용된다.
public class JpaRunner implements ApplicationRunner {

    @PersistenceContext
    EntityManager entityManager;    // JPA 핵심 클래스 (SpringBoot의 ApplicationContext와 같은 위상)

    //    @Transactional  // 메서드에만 붙일 수도 있다.
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account account = new Account();
        account.setUsername("WON");
        account.setPassword("hibernate");

        Study study = new Study();
        study.setName("Spring Data JPA");

        /* 양방향 관계 설정하기 */
        account.addStudy(study);


        // 하이버네이트의 가장 핵심적인 API는 Session이다.
        // 하이버네이트 Session을 통한 save
        Session session = entityManager.unwrap(Session.class);
        session.save(account);
        session.save(study);


        // JPA를 통한 save
//        entityManager.persist(account);
    }
}
