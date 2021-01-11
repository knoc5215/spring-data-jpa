package me.jumen.springdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void queryMakingTest() {
        this.createComment(100, "spring data jpa");
        this.createComment(55, "HIBERNATE SPRING");


        // IgnoreCase 대소문자 처리
        List<Comment> comments = commentRepository.findByCommentContainsIgnoreCase("Spring");   // upper(?)
        int commentsSize = comments.size();
        assertThat(comments.size()).isEqualTo(commentsSize);

        // GreaterThan
        comments = commentRepository.findByCommentContainsIgnoreCaseAndLikeCountGreaterThan("Spring", 10);   // upper(?)
        assertThat(comments.size()).isEqualTo(commentsSize);

        // DESC
        comments = commentRepository.findByCommentContainsIgnoreCaseOrderByLikeCountDesc("Spring");   // upper(?)
        assertThat(comments.size()).isEqualTo(commentsSize);
        assertThat(comments).first().hasFieldOrPropertyWithValue("likeCount", 100);

        // Pageable with PageRequest.of() method
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "LikeCount"));
        Page<Comment> page = commentRepository.findByCommentContainsIgnoreCase("Spring", pageRequest);
        assertThat(page.getNumberOfElements()).isEqualTo(commentsSize);
        assertThat(page).first().hasFieldOrPropertyWithValue("likeCount", 100);

        // Stream
        try (Stream<Comment> commentStream = commentRepository.findByCommentContainsIgnoreCaseAndLikeCountLessThan("Spring", 80)) {
            Comment comment = commentStream.findFirst().get();
            assertThat(comment.getLikeCount()).isEqualTo(55);
        }


    }

    private void createComment(int likeCount, String comment) {
        Comment newComment = new Comment();
        newComment.setComment(comment);
        newComment.setLikeCount(likeCount);
        commentRepository.save(newComment);
    }


}