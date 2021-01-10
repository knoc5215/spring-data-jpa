package me.jumen.springdatajpa;

import org.hibernate.Session;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

//@Component
@Transactional
public class AccountStudyRunner implements ApplicationRunner {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void run(ApplicationArguments args) {
        Study study = new Study();
        study.setName("Spring Data JPA");

        Account account = new Account();
        account.setUsername("WON");
        account.setPassword("hibernate");
        account.addStudy(study); /* 양방향 관계 설정하기 */

        /*
         * Persistent 상태 : JPA가 관리중인 상 (1차 캐시)
         * save를 호출한다고 해서 바로 INSERT 쿼리가 발생하는 것은 아니다.
         * save를 호출하면 1차 캐시 : PersistenceContext에 이 instance를 넣어서 캐싱한다.
         * */

//      entityManager.persist(account);   //JPA를 통한 save 방법
        Session session = entityManager.unwrap(Session.class);  /* 하이버네이트의 가장 핵심적인 API는 Session이다.*/

        session.save(account);
        session.save(study);

        Account load = session.load(Account.class, account.getId());
        load.setUsername("WON 1");
        load.setUsername("WON 2");
        load.setUsername("WON");


        /*
         * 이렇게 했는데도 update 쿼리가 실행되지 않는다.
         * 1. Dirty Checking (이 객체의 변경사항을 감시한다)
         * 2. Write Behind (객체의 변경사항을 최대한 늦은 시점에 반영한다.)
         *
         * 계속 감시하다 보니까 변경사항이 처음이랑 똑같다.(WON -> WON 1 -> WON 2 -> WON)
         * update 하지 않겠다.
         * */

        System.out.println("========== 현재 상태에서는 캐싱된 값을 보여주므로 이게 끝나고 INSERT가 동작한다. ==========");
        System.out.println("========== 실제 INSERT는 트랜잭션이 끝날때 발생한다. ==========");
        System.out.println(load.toString());
    }
}
