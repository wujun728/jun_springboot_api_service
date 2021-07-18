package io.basquiat;

import io.basquiat.user.model.User;
import io.basquiat.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {

    @Autowired
    private UserService userService;

    //@Test
    void testInsertUser() {
        User newUser = User.builder().name("사이먼 도미닉").age(36).build();
        Mono<User> mono = userService.createUser(newUser);
        StepVerifier.create(mono)
                    .assertNext(user-> assertThat(user.getId()).isEqualTo(1L))
                    .verifyComplete();
    }

    //@Test
    void testCustomInsertUser() {
        User newUser = User.builder().name("Simon Dominic").age(36).build();
        Mono<User> mono = userService.customCreateUser(newUser);
        StepVerifier.create(mono)
                    .assertNext(user-> assertThat(user.getId()).isEqualTo(2L))
                    .verifyComplete();
    }

    //@Test
    void testFindAll() {
        Flux<User> flux = userService.findAll();
        StepVerifier.create(flux)
                    .assertNext(user-> assertThat(user.getName()).isEqualTo("사이먼 도미닉"))
                    .assertNext(user-> assertThat(user.getName()).isEqualTo("Simon Dominic"))
                    .verifyComplete();
    }

    //@Test
    void testCustomFindAll() {
        Flux<User> flux = userService.customFindAll();
        StepVerifier.create(flux)
                    .assertNext(user-> assertThat(user.getName()).isEqualTo("사이먼 도미닉"))
                    .assertNext(user-> assertThat(user.getName()).isEqualTo("Simon Dominic"))
                    .verifyComplete();
    }

    //@Test
    void testCustomFindAllProjection() {
        Flux<User> flux = userService.customFindAllProjection();
        StepVerifier.create(flux)
                    .assertNext(user-> assertThat(user.getName()).isEqualTo("사이먼 도미닉"))
                    .assertNext(user-> assertThat(user.getName()).isEqualTo("Simon Dominic"))
                    .verifyComplete();
    }

    //@Test
    void testUpdateUser() {
        User updateUser = User.builder().id(1L).name("쌈디").age(36).build();
        Mono<User> mono = userService.updateUser(updateUser);
        StepVerifier.create(mono)
                    .assertNext(user-> assertThat(user.getName()).isEqualTo("쌈디"))
                    .verifyComplete();
    }

    //@Test
    void testCustomUpdateUser() {
        User updateUser = User.builder().id(1L).name("정기석").age(36).build();
        Mono<User> mono = userService.customUpdateUser(updateUser);
        StepVerifier.create(mono)
                    .assertNext(user-> assertThat(user.getName()).isEqualTo("정기석"))
                    .verifyComplete();
    }

    //@Test
    void testCustomUpdateUserByUntyped() {
        User updateUser = User.builder().id(1L).name("사이먼 도미닉").age(36).build();
        Mono<User> mono = userService.customUpdateUserByUntyped(updateUser);
        StepVerifier.create(mono)
                    .assertNext(user-> assertThat(user.getName()).isEqualTo("사이먼 도미닉"))
                    .verifyComplete();
    }

    //@Test
    void testDeleteUser() {
        User deleteUser = User.builder().id(1L).name("사이먼 도미닉").age(36).build();
        Mono<Void> mono = userService.deleteUser(deleteUser);
        StepVerifier.create(mono)
                    .verifyComplete();
    }

    //@Test
    void testCustomDeleteUser() {
        Mono<Void> mono = userService.customDeleteUser(2L);
        StepVerifier.create(mono)
                    .verifyComplete();
    }

    //@Test
    void testFindByNameAndAge() {
        Flux<User> flux = userService.findByNameAndAge("이센스", 33);
        StepVerifier.create(flux)
                    .assertNext(user-> assertThat(user.getName()).isEqualTo("이센스"))
                    .verifyComplete();
    }

    @Test
    void testFindByNameContaining() {
        Flux<User> flux = userService.findByNameContaining("센스");
        StepVerifier.create(flux)
                    .expectNextCount(2)
                    .verifyComplete();
    }

}
