package me.jumen.springdatajpa;

public interface CommentSummary {

    String getComment();

    int getUp();

    int getDown();

    /**
     * Open Projection의 장점을 살리면서 Closed 로 동작하게 하는 방법
     * 쿼리 최적화 + custom 구현체
     * 가장 좋은 방법이다.
     * */
    default String getVotes() {
        return getUp() + " " + getDown();
//        select
//        comment0_.comment as col_0_0_,
//                comment0_.up as col_1_0_,
//        comment0_.down as col_2_0_
//                from
//        comment comment0_
//        left outer join
//        post post1_
//        on comment0_.post_id=post1_.id
//        where
//        post1_.id=?
    }

    /**
     * Open Projection Interface
     * target은 Comment인데 한정짓지 못해서 다 가져오게 된다
     * */
//    @Value("#{target.up + ' ' + target.down}")
//    String getVotes();
}
