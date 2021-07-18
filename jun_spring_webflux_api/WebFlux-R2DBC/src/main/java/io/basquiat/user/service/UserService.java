package io.basquiat.user.service;

import io.basquiat.user.model.User;
import io.basquiat.user.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("userService")
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    /**
     * ReactiveCrudRepository의 findAll을 이용한 사용자 가져오기
     * @return Flux<User>
     */
    public Flux<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * 커스텀 레파지토리를 이용한 사용자 가져오기
     * @return Flux<User>
     */
    public Flux<User> customFindAll() {
        return userRepo.findAllUser();
    }

    /**
     * 커스텀 레파지토리를 이용해 프로젝션으로 사용자 가져오기
     * @return Flux<User>
     */
    public Flux<User> customFindAllProjection() {
        return userRepo.findAllUserProjection();
    }

    /**
     * ReactiveCrudRepository의 save을 이용한 사용자 수정하기
     * @param user
     * @return Mono<User>
     */
    public Mono<User> createUser(User user) {
        return userRepo.save(user);
    }

    /**
     * 커스텀 레파지토리를 이용한 사용자 저장하기
     * @param user
     * @return Mono<User>
     */
    public Mono<User> customCreateUser(User user) {
        return userRepo.createUser(user);
    }

    /**
     * ReactiveCrudRepository의 save(update)을 이용한 사용자 수정하기
     * @param user
     * @return Mono<User>
     */
    public Mono<User> updateUser(User user) {
        return userRepo.save(user);
    }

    /**
     * 커스텀 레파지토리를 이용한 사용자 수정하기
     * @param user
     * @return Mono<User>
     */
    public Mono<User> customUpdateUser(User user) {
        return userRepo.updateUser(user);
    }

    /**
     * 커스텀 레파지토리를 이용한 사용자 수정하기 (Untyped)
     * @param user
     * @return Mono<User>
     */
    public Mono<User> customUpdateUserByUntyped(User user) {
        return userRepo.updateUserByUntyped(user);
    }

    /**
     * ReactiveCrudRepository의 delete를 이용한 사용자 삭제
     * @param user
     * @return Mono<Void>
     */
    public Mono<Void> deleteUser(User user) {
        return userRepo.delete(user);
    }

    /**
     * 커스텀 레파지토리를 이용한 사용자 삭제
     * @param id
     * @return Mono<Void>
     */
    public Mono<Void> customDeleteUser(long id) {
        return userRepo.removeUser(id);
    }

    /**
     * dynamin method Chaining
     * @param name
     * @param age
     * @return Flux<User>
     */
    public Flux<User> findByNameAndAge(String name, int age) {
        return userRepo.findByNameAndAge(name, age);
    }

    /**
     * dynamin method Chaining
     * @param name
     * @return Flux<User>
     */
    public Flux<User> findByNameContaining(String name) {
        return userRepo.findByNameContaining(name);
    }

}
