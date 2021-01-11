package me.jumen.springdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

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
        List<Comment> all = commentRepository.findAll();
        assertThat(all.size()).isEqualTo(1);

        //When
        long count = commentRepository.count();
        //Then
        assertThat(count).isEqualTo(1);

    }

}