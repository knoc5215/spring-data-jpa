package me.jumen.springdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
        // slicing test using h2 database
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    @Rollback(false)    // @DataJpaTest의 @Transactional은 기본적으로 Rollback을 시킨다. 그러면 쿼리를 볼 수 없으니, Rollback=false로 설정한다.
    public void crudRepositoryTest() {
        //Given
        Post post = new Post();
        post.setTitle("hello spring boot common");

        //When
        assertThat(post.getId()).isNull();  // Transient status
        Post save = postRepository.save(post);

        //Then
        assertThat(save.getId()).isNotNull();

        //When
        List<Post> all = postRepository.findAll();

        //Then
        assertThat(all.size()).isEqualTo(1);
        assertThat(all).contains(save);

        //When
        Page<Post> postPage = postRepository.findAll(PageRequest.of(0, 10));

        //Then
        assertThat(postPage.getTotalElements()).isEqualTo(1);
        assertThat(postPage.getNumber()).isEqualTo(0);
        assertThat(postPage.getSize()).isEqualTo(10);
        assertThat(postPage.getNumberOfElements()).isEqualTo(1);

        //When
        Page<Post> titleContains = postRepository.findByTitleContains("spring", PageRequest.of(0, 10));
        //Then
        assertThat(titleContains.getTotalElements()).isEqualTo(1);
        assertThat(titleContains.getNumber()).isEqualTo(0);
        assertThat(titleContains.getSize()).isEqualTo(10);
        assertThat(titleContains.getNumberOfElements()).isEqualTo(1);

        //When
        long countByTitleContains = postRepository.countByTitleContains("spring");
        //Then
        assertThat(countByTitleContains).isEqualTo(1);

    }

    @Test
    public void customTest() {
        Post post = new Post();
        post.setTitle("custom");
        postRepository.save(post);  // select가 필요하다가고 hibernate가 판단하여 flushing이 일어나서 insert query exec
        postRepository.findMyPost();

        /**<delete query가 실행되지 않았던 이유>
         * entityManager가 removed 상태로 변경시켰지만, 실제로 DB sync는 하지 않는다.
         * @Transactional이 붙어있는 스프링의 모든 테스트는 기본적으로 ROLLBACK 트랜잭션이다.
         * Hibernate는 ROLLBACK 트랜잭션의 경우 필요없는 query는 실행하지 않는다.
         */
        postRepository.delete(post);

        postRepository.flush(); // 하지만 강제로 flush()를 호출하면 query를 실행한다.

    }

    @Autowired
    SendRepository sendRepository;

    @Test
    public void commonTest() {
        Post post = new Post();
        post.setTitle("CommonRepository");

        assertThat(sendRepository.contains(post)).isFalse();    // transient 상태

        sendRepository.save(post);

        assertThat(sendRepository.contains(post)).isTrue();    // persistent 상태
    }


}