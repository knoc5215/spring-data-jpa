package me.jumen.springdatajpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

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
     * @NamedQuery
     * Domain 클래스에 타입-세이프 하지않은 JPQL로 기입해야하기에 지저분해진다.
     * */
    //List<Post> findByTitle(String title);

    /**
     * @Query 깔끔하니까 권장한다.
     */
    @Query("SELECT p FROM Post AS p WHERE p.title = ?1")
    List<Post> findByTitle(String title);

    /**
     * Alias 줄 수 있음
     *
     * @Query("SELECT p, p.title AS pTitle FROM Post AS p WHERE p.title = ?1")
     */
//    @Query("SELECT p FROM Post AS p WHERE p.title = ?1")
//    List<Post> findByTitle(String title, Sort sort);

    /**
     * @Named Parameter
     * @Param을 이용한 ?1 채번 대체
     */
    @Query("SELECT p FROM Post AS p WHERE p.title = :title")
    List<Post> findByTitle(@Param("title") String title, Sort sort);


    /**
     * SpEL
     *
     * @Query에서 엔티티 이름을 미리정의가 되어있는 #{#entityName} 으로 표현할 수 있다
     * @Entity("entityName")으로 이름을 바꿔도, SpEL을 통해 @Query의 FROM 을 수정할 필요가 없다.
     */
//    @Query("SELECT p FROM #{#entityName} AS p WHERE p.title = :title")
//    List<Post> findByTitle(@Param("title") String title, Sort sort);


    @Modifying(clearAutomatically = true, flushAutomatically = true)    // flush -> update query -> PersistentContext를 clear -> DB에서 가져온다(context를 비워줘서 캐시에 없으니까)
    @Query("UPDATE Post p Set p.title = ?1 WHERE p.id = ?2")
    int updateTitle(String hibernate, Long id);


}
