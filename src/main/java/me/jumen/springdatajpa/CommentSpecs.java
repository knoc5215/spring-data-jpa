package me.jumen.springdatajpa;

import org.springframework.data.jpa.domain.Specification;

public class CommentSpecs {

    // comment가 best인지 판단하는 스펙
    public static Specification<Comment> isBest() {
        return (root, query, builder) ->
                builder.isTrue(root.get(Comment_.best));
    }

    // comment가 up이 10이상인지 판단하는 스펙
    public static Specification<Comment> isGood() {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get(Comment_.up), 10);
    }
}
