package me.jumen.springdatajpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA의 기본적인 동작 원리와 빈 등록
 *
 * @Repository가 없어도 Bean으로 등록해 줌
 * @EnableJpaRepositories 여기서부터 시작한다
 * EnableJpaRepositories 안에 @Import(JpaRepositoriesRegistrar.class)로 시작해서
 * RepositoryBeanDefinitionRegistrarSupport 이 implements ImportBeanDefinitionRegistrar(핵심) 하고
 * @Override registerBeanDefinitions() method
 */
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository<Post> {

    Page<Post> findByTitleContains(String title, Pageable pageable);

    long countByTitleContains(String title);

}
