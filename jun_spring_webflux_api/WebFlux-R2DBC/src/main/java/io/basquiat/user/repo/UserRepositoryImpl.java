package io.basquiat.user.repo;

import io.basquiat.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.r2dbc.query.Criteria.where;
import static org.springframework.data.relational.core.query.Update.update;


@AllArgsConstructor
public class UserRepositoryImpl implements CustomUserRepository {

    private final DatabaseClient query;
    private final ReactiveDataAccessStrategy dataAccessStrategy;

    @Override
    public Flux<User> findAllUser() {
        return query.select()
                    .from("basquiat_user")
                    .as(User.class)
                    .fetch()
                    .all();
    }

    @Override
    public Flux<User> findAllUserProjection() {
        return query.select()
                    .from("basquiat_user")
                    .as(User.class)
                    .project("user_name", "user_age")
                    .fetch()
                    .all();
    }

    @Override
    public Mono<User> createUser(User user) {
        return query.insert()
                    .into(User.class)
                    .using(user)
                    .map(this.dataAccessStrategy.getConverter().populateIdIfNecessary(user))
                    .first()
                    .defaultIfEmpty(user);
    }

    @Override
    public Mono<User> updateUser(User user) {
        return query.update()
                    .table(User.class)
                    .using(user)
                    .then()
                    .then(Mono.just(user));
    }

    @Override
    public Mono<User> updateUserByUntyped(User user) {
        return query.update()
                    .table("basquiat_user")
                    .using(update("user_name", user.getName()).set("user_age", user.getAge()))
                    .matching(where("id").is(user.getId()))
                    .then()
                    .then(Mono.just(user));
    }


    @Override
    public Mono<Void> removeUser(long id) {
        return query.delete()
                    .from(User.class)
                    .matching(where("id").is(id))
                    .then();
    }

}
