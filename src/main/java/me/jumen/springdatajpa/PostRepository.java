package me.jumen.springdatajpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

/**
 * JPA의 기본적인 동작 원리와 빈 등록
 *
 * @Repository가 없어도 Bean으로 등록해 줌
 * @EnableJpaRepositories 여기서부터 시작한다
 * EnableJpaRepositories 안에 @Import(JpaRepositoriesRegistrar.class)로 시작해서
 * RepositoryBeanDefinitionRegistrarSupport 이 implements ImportBeanDefinitionRegistrar(핵심) 하고
 * @Override registerBeanDefinitions() method
 */
public interface PostRepository extends YourRepository<Post, Long>, QuerydslPredicateExecutor<Post> {

    Page<Post> findByTitleContains(String title, Pageable pageable);

    long countByTitleContains(String title);

    List<Post> findByTitleStartsWith(String title);

    /**
     * NamedQuery도 Domain 클래스에 타입-세이프 하지않은 JPQL로 기입해야하기에 지저분해진다.
     * */
    //List<Post> findByTitle(String title);

    /**
     * 깔끔한 @Query를 권장한다.
     * */
    @Query("SELECT p FROM Post AS p WHERE p.title = ?1")
    List<Post> findByTitle(String title);

}
