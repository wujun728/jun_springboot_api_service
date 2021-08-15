package io.basquiat.user.repo;

import io.basquiat.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by basquiat
 *
 * Custom User Repository
 *
 */
public interface CustomUserRepository {
    Flux<User> findAllUser();
    Flux<User> findAllUserProjection();
    Mono<User> createUser(User user);
    Mono<User> updateUser(User user);
    Mono<User> updateUserByUntyped(User user);
    Mono<Void> removeUser(long id);
}
