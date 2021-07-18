package io.basquiat.user.web;

import io.basquiat.user.model.User;
import io.basquiat.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
@Api(value = "User Controller", tags = {"User Controller"})
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    @ApiOperation(value = "사용자 생성 API")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@RequestBody User user) {
        Assert.notNull(user, "must be user");
        return userService.createUser(user);
    }

    @PostMapping("/customuser")
    @ApiOperation(value = "사용자 생성 API")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> customCreateUser(@RequestBody User user) {
        Assert.notNull(user, "must be user");
        return userService.customCreateUser(user);
    }

    @GetMapping("/user")
    @ApiOperation(value = "사용자 조회 API")
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/customuser")
    @ApiOperation(value = "사용자 조회 API")
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> customFindAll() {
        return userService.customFindAll();
    }

    @GetMapping("/customuser/projection")
    @ApiOperation(value = "사용자 조회 API")
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> customFindAllProjection() {
        return userService.customFindAllProjection();
    }

    @GetMapping("/user/{name}/{age}")
    @ApiOperation(value = "사용자 조회 API")
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> findByNameAndAge(@PathVariable(value = "name", required = true) String name,
                                       @PathVariable(value = "age", required = true) int age) {
        return userService.findByNameAndAge(name, age);
    }

    @GetMapping("/user/{name}")
    @ApiOperation(value = "사용자 조회 API")
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> findByNameAndAge(@PathVariable(value = "name", required = true) String name) {
        return userService.findByNameContaining(name);
    }

    @PatchMapping("/user")
    @ApiOperation(value = "사용자 수정 API")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> update(@RequestBody User user) {
        Assert.notNull(user, "must be user");
        return userService.updateUser(user);
    }

    @PatchMapping("/customuser")
    @ApiOperation(value = "사용자 수정 API")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> customUpdate(@RequestBody User user) {
        Assert.notNull(user, "must be user");
        return userService.customUpdateUser(user);
    }

    @PatchMapping("/customuser/untyped")
    @ApiOperation(value = "사용자 수정 API")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> customUpdateByUntyped(@RequestBody User user) {
        Assert.notNull(user, "must be user");
        return userService.customUpdateUserByUntyped(user);
    }

    @DeleteMapping("/user")
    @ApiOperation(value = "사용자 삭제 API")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@RequestBody User user) {
        Assert.notNull(user, "must be user");
        return userService.deleteUser(user);
    }

    @DeleteMapping("/user/{id}")
    @ApiOperation(value = "사용자 삭제 API")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable(value = "id", required = true) long id) {
        Assert.notNull(id, "must be user id");
        return userService.customDeleteUser(id);
    }

}
