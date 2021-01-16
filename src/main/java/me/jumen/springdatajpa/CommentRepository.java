package me.jumen.springdatajpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public interface CommentRepository extends MyRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    /**
     * 기본값은 JPQL
     * nativeQuery 설정 가능
     */
    //@Query(value = "SELECT c FROM Comment AS c", nativeQuery = true)
    List<Comment> findByCommentContainsIgnoreCase(String keyword);

    List<Comment> findByCommentContainsIgnoreCaseAndLikeCountGreaterThan(String keyword, int likeCount);

    List<Comment> findByCommentContainsIgnoreCaseOrderByLikeCountDesc(String keyword);

    Page<Comment> findByCommentContainsIgnoreCase(String keyword, Pageable pageable);

    Stream<Comment> findByCommentContainsIgnoreCaseAndLikeCountLessThan(String keyword, int likeCount);

    @Async
    Future<List<Comment>> findByCommentContainsIgnoreCaseOrderByIdDesc(String keyword);

//    @EntityGraph(value = "Comment.post")
    @EntityGraph(attributePaths = {"post"})
    Optional<Comment> getById(Long id);

//    List<Comment> findByPost_id(Long id);

//    List<CommentSummary> findByPost_id(Long id);

    /* Dynamic Projection */
    @Transactional(readOnly = true)
    <T> List<T> findByPost_id(Long id, Class<T> type);

}
