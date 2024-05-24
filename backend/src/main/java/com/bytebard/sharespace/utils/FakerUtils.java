package com.bytebard.sharespace.utils;

import com.bytebard.sharespace.models.Post;
import com.bytebard.sharespace.models.User;
import com.bytebard.sharespace.repository.PostRepository;
import com.bytebard.sharespace.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class FakerUtils implements CommandLineRunner {

    public final UserRepository userRepository;
    public final PostRepository postRepository;

    public FakerUtils(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public void generateFakeData() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Faker faker = new Faker();
        var password = passwordEncoder.encode("Password1@");
        for (int i = 0; i < 50; i++) {
            User user = new User(
                    null,
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    password,
                    faker.name().username(),
                    faker.lorem().fixedString(20),
                    faker.avatar().image(),
                    faker.name().name()
            );
            user = userRepository.save(user);
            List<Post> posts = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                Post post = new Post(
                        null,
                        faker.book().title(),
                        faker.internet().image(),
                        faker.artist().name(),
                        faker.friends().location(),
                        String.join(",", faker.book().genre(), faker.music().genre()),
                        null,
                        user,
                        null,
                        null
                );
                posts.add(post);
            }

            postRepository.saveAll(posts);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        generateFakeData();
    }
}
