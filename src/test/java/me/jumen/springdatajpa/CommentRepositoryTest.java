package me.jumen.springdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Test
    public void crudRepositoryTest() {
        //When
        Comment comment = new Comment();
        comment.setComment("Hello Comment");
        commentRepository.save(comment);

        //Then
        List<Comment> all = commentRepository.findAll();    // Collection은 null이 아닌, empty인 것을 리턴한다.
        assertThat(all.size()).isEqualTo(1);

        //When
        long count = commentRepository.count();
        //Then
        assertThat(count).isEqualTo(1);


    }

    @Test
    public void optionalTest() {
        //When
        Optional<Comment> byId = commentRepository.findById(100l);
        assertThat(byId).isEmpty(); // optional은 null을 리턴하지 않기에 empty를 체크해야 함.


    }

}