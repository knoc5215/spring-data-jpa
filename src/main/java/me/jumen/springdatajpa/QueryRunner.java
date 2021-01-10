package me.jumen.springdatajpa;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

/*
 * @Transactional - EntityManager 와 관련된 모든 행위들은 한 트랜잭션 안에서 일어나야 한다.
 * 1. 클래스에 붙이면 모든 메서드에 적용된다.
 * 2. 메서드에만 붙일 수도 있다.
 * */

@Component
@Transactional
public class QueryRunner implements ApplicationRunner {

    @PersistenceContext
    EntityManager entityManager;    // JPA 핵심 클래스 (SpringBoot의 ApplicationContext와 같은 위상)


    @Override
    public void run(ApplicationArguments args) throws Exception {
        /**
         * JPQL - 타입세이프 X
         */
        TypedQuery<Post> query = entityManager.createQuery("SELECT p FROM Post AS p", Post.class);
        List<Post> jpqlResultList = query.getResultList();
        jpqlResultList.forEach(p -> System.out.println(p.toString()));

        /**
         * Criteria - 타입세이프 O
         * */
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> criteriaQuery = builder.createQuery(Post.class);
        Root<Post> from = criteriaQuery.from(Post.class);
        criteriaQuery.select(from);

        List<Post> criteriaResultList = entityManager.createQuery(criteriaQuery).getResultList();
        criteriaResultList.forEach(p -> System.out.println(p.toString()));

        /**
         * NaemdQuery
         * */
        entityManager.createNamedQuery("all_post", Post.class);

        /**
         * Native Query
         * */
        List<Post> nativeQueryResultList = entityManager.createNativeQuery("SELECT * FROM POST", Post.class).getResultList();
        nativeQueryResultList.forEach(p -> System.out.println(p.toString()));

    }
}
