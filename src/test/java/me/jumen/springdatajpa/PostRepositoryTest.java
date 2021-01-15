package me.jumen.springdatajpa;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest// slicing test using h2 database
@Import(PostRepositoryTestConfig.class)
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
        postRepository.findAll();

        /**<delete query가 실행되지 않았던 이유>
         * entityManager가 removed 상태로 변경시켰지만, 실제로 DB sync는 하지 않는다.
         * @Transactional이 붙어있는 스프R링의 모든 테스트는 기본적으로 ROLLBACK 트랜잭션이다.
         * Hibernate는 ROLLBACK 트랜잭션의 경우 필요없는 query는 실행하지 않는다.
         */
        postRepository.delete(post);

        postRepository.flush(); // 하지만 강제로 flush()를 호출하면 query를 실행한다.

    }


    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void eventTest() {
        //Given
        Post post = new Post();
        post.setTitle("post event publish");
        //When
        postRepository.save(post.publish());

    }

    @Test
    public void querydslTest() {
        //Given
        Post post = new Post();
        post.setTitle("post querydsl");
        postRepository.save(post.publish());

        //When
        Predicate predicate = QPost.post.title.contains("query");
        Optional<Post> one = postRepository.findOne(predicate);
        //Then
        assertThat(one).isNotEmpty();

    }

    @PersistenceContext
    EntityManager entityManager;

    /**
     * save()
     * 새로운 객체라면 persist()를 호출한다.
     * 새로운 객체가 아닐 경우는 merge()를 호출한.
     */
    @Test
    public void saveAndUpdateTest() {
        /**
         * @Id가 null이면 Transient 상태로 판단
         * */
        Post post = new Post();
        post.setTitle("JPA");
        Post save = postRepository.save(post);// persist -> insert

        // post가 PersistenceContext에서 관리(캐싱)되는 상태이기 때문
        assertThat(entityManager.contains(save)).isTrue();
        assertThat(entityManager.contains(post)).isTrue();
        assertThat(post == save);


        /**
         * @Id가 not null이면 Detached 상태로 판단
         * */
        Post postUpdate = new Post();
        postUpdate.setId(post.getId());
        postUpdate.setTitle("HIBERNATE");
        // 리턴받은 updatedPost가 영속화된다.
        // merge에 넘긴 entity의 복사본을 만들고, 이를 persistent 상태로 변경하고 이 복사본을 리턴한다
        // DB에 sync를 하기에 상태변화를 DB에 update 한다 (Id가 DB에 해당하는게 없다면 INSERT)
        Post updatedPost = postRepository.save(postUpdate);// merge -> update

//        postUpdate.setTitle("isTransient"); // postUpdate는 Transient 상태라 PersistentContext가 관리대상이 아니라서, 실제 save가 발생하는 시점(Write Behind)에 반영되지 않는다.
        updatedPost.setTitle("isPersistent");   // updatedPost는 persistent 상태이기에, write behind 시점에 이 부분이 반영된다.
        assertThat(entityManager.contains(updatedPost)).isTrue();   // 영속화 O
        assertThat(entityManager.contains(postUpdate)).isFalse();    // 영속화 X
        assertThat(post == save);

        List<Post> all = postRepository.findAll();
        assertThat(all.size()).isEqualTo(1);

        /**
         * Best practice는 parameter로 전달한 instance 대신에
         * return받은 instance를 항상 사용하는 것이다.
         * */


    }

    @Test
    @DisplayName("startWith Title 테스트")
    public void findByTitleStartsWith() {
        savePost();

        List<Post> byTitleStartsWith = postRepository.findByTitleStartsWith("Spring");
        assertThat(byTitleStartsWith.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("@NamedQuery, @Query 테스트")
    public void findByTitleNamedQueryAndQuery() {
        savePost();

        List<Post> byTitle = postRepository.findByTitle("Spring Data Jpa");
        assertThat(byTitle.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("@Query Sort 테스트")
    public void findByTitleQuerySort() {
        savePost();

        List<Post> byTitle = postRepository.findByTitle("Spring Data Jpa", Sort.by("title"));   // property, alias 아니라면 error
        assertThat(byTitle.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("@Query Sort Jpa.unsafe() 테스트")
    public void findByTitleQuerySortJpaUnsafe() {
        savePost();

        List<Post> byTitle = postRepository.findByTitle("Spring Data Jpa", JpaSort.unsafe("LENGTH(title)"));
        assertThat(byTitle.size()).isEqualTo(1);
    }

    private void savePost() {
        Post post = new Post();
        post.setTitle("Spring Data Jpa");
        Post save = postRepository.save(post);
    }


}