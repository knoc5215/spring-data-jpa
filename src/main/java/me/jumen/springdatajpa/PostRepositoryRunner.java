package me.jumen.springdatajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class PostRepositoryRunner implements ApplicationRunner {

    @Autowired
    Jumen jumen;

    PostRepository postRepository;

    public PostRepositoryRunner(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public void run(ApplicationArguments args) {
        System.out.println("=============" + jumen.getName());
        postRepository.findAll().forEach(p -> {
            System.out.println(p.toString());
        });
    }
}
