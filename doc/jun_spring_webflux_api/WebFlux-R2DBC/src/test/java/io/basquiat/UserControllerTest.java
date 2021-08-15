package io.basquiat;

import io.basquiat.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    //@Test
    void testInsertUser() {
        User newUser = User.builder().name("이센스111").age(33).build();
        webTestClient.post()
                     .uri("/v1/user")
                     .contentType(MediaType.APPLICATION_JSON)
                     .accept(MediaType.APPLICATION_JSON)
                     .body(Mono.just(newUser), User.class)
                     .exchange()
                     .expectStatus().isCreated()
                     .expectHeader().contentType(MediaType.APPLICATION_JSON)
                     .expectBody()
                     .jsonPath("$.id").isEqualTo(1);
    }

    //@Test
    void testCustomInsertUser() {
        User newUser = User.builder().name("이센스111").age(33).build();
        webTestClient.post()
                     .uri("/v1/customuser")
                     .contentType(MediaType.APPLICATION_JSON)
                     .accept(MediaType.APPLICATION_JSON)
                     .body(Mono.just(newUser), User.class)
                     .exchange()
                     .expectStatus().isCreated()
                     .expectHeader().contentType(MediaType.APPLICATION_JSON)
                     .expectBody()
                     .jsonPath("$.id").isEqualTo(2);
    }

    //@Test
    void testFindAll() {
        webTestClient.get().uri("/v1/user")
                           .exchange()
                           .expectStatus().isOk()
                           .expectBodyList(User.class).hasSize(2);
    }

    //@Test
    void testCustomFindAll() {
        webTestClient.get().uri("/v1/customuser")
                     .exchange()
                     .expectStatus().isOk()
                     .expectBodyList(User.class).hasSize(3); // 실패를 예상한다.
    }

    //@Test
    void testCustomFindAllProjection() {
        webTestClient.get().uri("/v1/customuser/projection")
                     .exchange()
                     .expectStatus().isOk()
                     .expectBodyList(User.class).hasSize(2); // 실패를 예상한다.
    }

    //@Test
    void testUpdateUser() {
        User updateUser = User.builder().id(2L).name("e-sense").age(33).build();

        webTestClient.patch().uri("/v1/user")
                             .contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .body(Mono.just(updateUser), User.class)
                             .exchange()
                             .expectStatus().isOk()
                             .expectHeader().contentType(MediaType.APPLICATION_JSON)
                             .expectBody()
                             .jsonPath("$.name").isEqualTo("e-sense");
    }

    //@Test
    void testCustomUpdateUser() {
        User updateUser = User.builder().id(2L).name("E-SENSE").age(32).build();

        webTestClient.patch().uri("/v1/customuser")
                             .contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .body(Mono.just(updateUser), User.class)
                             .exchange()
                             .expectStatus().isOk()
                             .expectHeader().contentType(MediaType.APPLICATION_JSON)
                             .expectBody()
                             .jsonPath("$.name").isEqualTo("E-SENSE");
    }

    //@Test
    void testCustomUpdateUserByUntyped() {
        User updateUser = User.builder().id(2L).name("ESENSE").age(33).build();

        webTestClient.patch().uri("/v1/customuser/untyped")
                             .contentType(MediaType.APPLICATION_JSON)
                             .accept(MediaType.APPLICATION_JSON)
                             .body(Mono.just(updateUser), User.class)
                             .exchange()
                             .expectStatus().isOk()
                             .expectHeader().contentType(MediaType.APPLICATION_JSON)
                             .expectBody()
                             .jsonPath("$.name").isEqualTo("ESENSE");
    }

    //@Test
    void testDeleteUser() {
        User updateUser = User.builder().id(2L).name("ESENSE").age(33).build();
        webTestClient.method(HttpMethod.DELETE)
                     .uri("/v1/user")
                     .body(BodyInserters.fromProducer(Mono.just(updateUser), User.class))
                     .exchange()
                     .expectStatus().isNoContent()
                     .expectBody().isEmpty();

    }

    //@Test
    void testCustomDeleteUser() {
        webTestClient.delete()
                     .uri("/v1/user/{id}", 1L)
                     .exchange()
                     .expectStatus().isNoContent()
                     .expectBody().isEmpty();
    }

    //@Test
    void testFindByNameAndAge() {
        webTestClient.get()
                     .uri("/v1/user/{name}/{age}", "팔로알토", 36)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBodyList(User.class).hasSize(1);
    }

    @Test
    void testFindByNameContaining() {
        webTestClient.get()
                     .uri("/v1/user/{name}", "센스")
                     .exchange()
                     .expectStatus().isOk()
                     .expectBodyList(User.class).hasSize(2);
                     //.expectBodyList(User.class).hasSize(1); //실패 예상
    }

}
