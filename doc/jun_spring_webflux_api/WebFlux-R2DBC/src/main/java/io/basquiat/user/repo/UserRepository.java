package io.basquiat.user.repo;

import io.basquiat.user.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<User, Long>, CustomUserRepository {

    /**
     * name과 age로 찾는다.
     * @param name
     * @param age
     * @return Flux<User>
     */
    Flux<User> findByNameAndAge(String name, int age);

    /**
     * like 검색
     * @param name
     * @return Flux<User>
     */
    Flux<User> findByNameContaining(String name);

}