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


}