# Spring Boot WebFlux With R2DBC      

# OTHER BRANCH

[혼자서북치고장구치기 1](https://github.com/basquiat78/WebFlux-R2DBC/tree/1.%ED%98%BC%EC%9E%90%EC%84%9C%EB%B6%81%EC%B9%98%EA%B3%A0%EC%9E%A5%EA%B5%AC%EC%B9%98%EA%B8%B0)

# Issue 정리     
심심해서 spring-data-jpa처럼 연쇄적으로 다이나믹하게 메소드를 생성하는것이 가능하지 않을까 해서 테스트 해보다가 되는 것을 보고 깜놀.....     

```
INSERT INTO basquiat_user (user_name, user_age) VALUES ('사이먼 도미닉', 36);
INSERT INTO basquiat_user (user_name, user_age) VALUES ('이센스', 33);
INSERT INTO basquiat_user (user_name, user_age) VALUES ('팔로알토', 36);
INSERT INTO basquiat_user (user_name, user_age) VALUES ('키드밀리', 26);
INSERT INTO basquiat_user (user_name, user_age) VALUES ('이 센스', 33);
```
일단 다음과 같이 데이터를 밀어 넣는다.

UserRepository

```
definitions or references in the same repository. Learn more

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
```
이름과 나이로 찾는 것, 그리고 이름으로 like검색을 하는 메소드를 작성한다.

UserService에 다음 2개의 코드를 넣는다.

```
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
```
다음과 같이 테스트를 실행해 보자.

```$xslt
@Test
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
```

UserController에도 다음과 같이 하나 넣어보자.

```
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
```

UserControllerTest

```
@Test
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
```
이게 된다.

```
DEBUG 98678 --- [actor-tcp-nio-1] o.s.d.r2dbc.core.NamedParameterExpander  : Expanding SQL statement [SELECT basquiat_user.id, basquiat_user.user_name, basquiat_user.user_age FROM basquiat_user WHERE basquiat_user.user_name LIKE $1] to [SELECT basquiat_user.id, basquiat_user.user_name, basquiat_user.user_age FROM basquiat_user WHERE basquiat_user.user_name LIKE $1]
```
NamedParameterExpander가 이와 관련된 클래스인듯 싶다.     

작성하기 전에 한번 테스트라도 해보고 쓸걸 그랬나보다.

## Prerequisites     

1. OS: macOS Catalina v10.15.5
2. Java: OpenJDK(AdoptOpenJDK) 1.8.0.222
3. IDE: IntelliJ IDEA 2020.1.2 (Community Edition)
4. Plugin: Lombok
5. Database: PostgreSQL 12.3
6. Spring Boot: 2.3.2.RELEASE
7. Swagger : springfox-boot-starter 3.0.0

## Configuration Swagger     
기존에 WebFlux에서 스웨거를 사용하는게 좀 많이 불편했었다.      

레파지토리의 경우에는 jcenter에 url을 직접적으로 넣어야 했고 dependency를 걸어야 할 라이브러리도 3,4개였다.      

또한 자바 설정 파일도 만들어야 했다.      

근데 예전 방식대로 세팅을 할려니 보지 못했던 deprecated된 어노테이션을 보고 좀 의아했다.      

그래서 해당 github의 예제와 공식 문서를 다 뒤저가면서 삽질을 했는데 편해졌다.     

그래서 이전 방식처럼 번거롭지 않다.     

그냥 

```
repositories {
	mavenCentral()

	jcenter()
}

.
.
.
.

implementation 'io.springfox:springfox-boot-starter:3.0.0'
```
이렇게만 설정만 하면 된다.     

예전에는 

```
@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfiguration {
	@Bean
	public Docket api() { 
		return new Docket(DocumentationType.SWAGGER_2).select()
                                          .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                                          .build()
                                          .genericModelSubstitutes(Optional.class, Flux.class, Mono.class);
	} 
}
```
이런 녀석도 만들어 줘야 하고 

```
@Configuration
public class WebfluxConfiguration implements WebFluxConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui.html**")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
```
이런 녀석도 설정을 해줘야 화면을 띄울 수 있지만 지금은 다 필요없다.     

다만 다음과 같이 application.yml에

```
springfox:
  documentation:
    swagger-ui:
      base-url: /documentation
```
이렇게 자신이 원하는 이름으로 경로를 작성하면          

http://localhost:8080/documentation/swagger-ui/index.html

또는

http://localhost:8080/documentation/swagger-ui/

요렇게 접근이 가능하다.     

```
@EnableSwagger2WebFlux <-- @deprecated
```

일단 나의 그레이들 설정은 다음과 같다.     

```
plugins {
	id 'org.springframework.boot' version '2.3.2.RELEASE'
	id 'io.spring.dependency-management' version '1.0.9.RELEASE'
	id 'java'
}

group = 'io.basquiat'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()

	jcenter()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc'
	implementation 'org.springframework.boot:spring-boot-starter-webflux'
	implementation 'org.springframework.boot:spring-boot-starter-freemarker'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'io.r2dbc:r2dbc-postgresql'
	runtimeOnly 'org.postgresql:postgresql'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation('org.springframework.boot:spring-boot-starter-test') {
		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
	}
	testImplementation 'io.projectreactor:reactor-test'
	developmentOnly "org.springframework.boot:spring-boot-devtools"

	implementation 'io.springfox:springfox-boot-starter:3.0.0'

}

test {
	useJUnitPlatform()
}
```

## thymeleaf vs freemarker
뭐 딱히... 정답이 있나? 둘다 써 봤는데 다 비슷하고 내 기준에서는 freemarker가 좀 나은거 같아서 나는 freemarker를 선호한다.    

어짜피 뭐 front-end를 이걸로 하는 데가 있나? 그냥 테스트용 프로젝트에서 쉽고 가볍게 사용할 수 있어서 쓰는거지....       

설마 있는건 아니겠지?       

## Intro     
이 프로젝트는 R2DBC에 대해서 조금 맛보는 것이다.     

그리고 내가 생각하는 것들을 하나씩 확장해 나가는 형식으로 바꿔 나갈 예정이다.     

사실 나는 WebFlux와 JPA 또는 myBatis조합에서 non-blocking을 별도의 설정과 메소드를 만들어서 사용하지 않고 마치 mongoDB처럼 쉽게 사용하는 것을 바랬다.    

하지만 아는 분들은 아시겠지만 R2DBC는 ORM이 아니다.      

mongoDB와 WebFlux를 조합해서 사용하신 분들이라면 몽고디비에서는 JPA에서 말하는 엔티티를 도큐먼트라고 지칭하는 것을 알 수 있다.     

근데 이것이 그거랑 좀 많이 비슷하다. **THIS IS NOT A ENTITY!, BUT LIKE**         

그리고 제공하는 Repository를 상속받아서 사용하기만 하면 쉽게 코드를 구현할 수 있게 되어 있다.     

초창기에는 JPA와 사용할 때 자료를 찾다 보니     

```
Flux.fromIterable() Or Mono.just()
```
외국 블로그든 국내 블로그등 이 딴걸로 메소드를 감싸서 리턴하는 방식으로 처리했다고 하는 글들이 많았는데....     

그래서 나의 깃헙의 초기 WebFlux관련 프로젝트나 이전 회사에서 작업한 수많은 작업물이 이렇게 한걸 생각하면 ~~흑흑흑~~     

어째든 저것들은 따라가면 저건 단지 랩퍼나 컨버터에 더 가깝다. 그냥 POJO나 Collection같은 객체를 비동기로 처리 하기 위해 리액티브 스트림즈로 변환시켜준다.          

따라서 

```
Flux.fromIterable(repo.findAll())
```
이 코드는 바로 List<T> 타입을 반환하는 repo.findAll()가 호출되는 시점, 즉 DB io가 발생하는 순간 비동기에서 동기로 바뀐다.     

이것을 코드로 증명하는 블로그가 있었는데 찾지 못하겠다. 찾으면 링크 한번 걸어보겠다.        

다만 에러가 나지 않고 그냥 흘러갈 뿐 결국 우리가 원하는 완벽한 비동기는 아니라는 것이다.     

결국 자바 8에서 지원하는 CompletableFuture.supplyAsync나 

```
Mono blockingWrapper = Mono.fromCallable(() -> { 
    return /* make a remote synchronous call */ 
});
blockingWrapper = blockingWrapper.subscribeOn(Schedulers.boundedElastic()); 
```
이런 녀석을 만들고 감싸야 한다.     

하지만 이것도 쭉 따라가다 곰곰히 생각해 보면 별도의 쓰레드를 생성해서 돌리는 방식이다.      

결국 쓰레드라는 리소스를 사용하는 것인데 이것도 리퀘스트가 많아지면 어떻게 되려나?      

물론 Async, ThreadPool설정을 통해서 이것을 해소할 수 있다고 해도.... 뭔가 번거롭다.        

사실 그래서 이것을 해결하기 위해 나온게 R2DBC이다.     

작년 초중반에는 마이너 버전이었는데 지금은 스프링 진형에서 spring-boot-starter-data-r2dbc을 제공한다.      

현재, 2020-07-29 기준으로 spring-boot-starter-data-r2dbc의 버전은 1.1.2.RELEASE이다.     

그러나... 내부적으로 땡겨오는 r2dbcsms 0.8x대로 여전히 마이너버전이다. ~~믿고 쓸수 있어? 결국 뭔가 변경되겠지? 갈길이 뭐네?~~

## Why Postgres?

일단 내 컴터에 깔려 있어서이다. 별 이유 없다.     

또한 https://r2dbc.io/ 사이트의 Drivers 목록에 보면 사실 mySQL이 있지만 MariaDB와 PostgreSQL의 경우에는 해당 벤더가 공식적으로 작업한 것이다.     

따라서 좀 더 안정적인 드라이버를 위해서 선택한 것 뿐이다.      

## NOT ORM??    

```
Spring Data R2DBC aims at being conceptually easy.      
In order to achieve this it does NOT offer caching, lazy loading, write behind or many other features of ORM frameworks.      
This makes Spring Data R2DBC a simple, limited, opinionated object mapper.     
```
spring.io의 Spring Data R2DBC의 일부를 발췌한 내용이다.     

caching, lazy loading, write behind or many other features of ORM frameworks <- NOT이라는 표현으로 강조하고 있다!!!!     

~~비슷한 컨셉을 가지고 있다. 하지만 spring-data-jpa처럼 연쇄적인 메소드 이름으로 쿼리를 생성하는 기능을 아직까지는 지원하지 않는다.~~   

~~기본적인 컨셉 가령 findById, existById, save, saveAll같은 녀석들만 지원한다.~~      

~~언젠가는 spring-data-jpa처럼 변경될려나?~~          

```
findByIdAndPositionAndNameContain....()
```
~~이런거 없다.~~     

위에 말이 틀렸다. 가능하다.....

그리고 queryDSL이나 Criteria같은 방식의 쿼리 작성을 지원한다.      

하지만 이것도 제한적이다. join같은 것은 네이티브 쿼리로 작성해야 한다.            

그렇다고 JPQL을 바라지 마라. 말 그대로 네이티브 쿼리를 쓴다.           

이 말인즉, 쿼리를 잘 알아야 한다는 것이다.      

## First Impressions     
첫 인상은 이렇다.      

'일단 우리는 spring-data, jpa의 컨셉들을 차용해서 비슷하게는 말들었어.'       

## Custom Repository

WebFlux와 관련해서 spring-data에서 제공하는 것은 다음과 같다.

```
/*
 * Copyright 2016-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.repository.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.reactivestreams.Publisher;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * Interface for generic CRUD operations on a repository for a specific type. This repository follows reactive paradigms
 * and uses Project Reactor types which are built on top of Reactive Streams.
 *
 * @author Mark Paluch
 * @author Christph Strobl
 * @since 2.0
 * @see Mono
 * @see Flux
 */
@NoRepositoryBean
public interface ReactiveCrudRepository<T, ID> extends Repository<T, ID> {
	<S extends T> Flux<S> saveAll(Iterable<S> entities);
	<S extends T> Flux<S> saveAll(Publisher<S> entityStream);
	Mono<T> findById(ID id);
	Mono<T> findById(Publisher<ID> id);
	Mono<Boolean> existsById(ID id);
	Mono<Boolean> existsById(Publisher<ID> id);
	Flux<T> findAll();
	Flux<T> findAllById(Iterable<ID> ids);
	Flux<T> findAllById(Publisher<ID> idStream);
	Mono<Long> count();
	Mono<Void> deleteById(ID id);
	Mono<Void> deleteById(Publisher<ID> id);
	Mono<Void> delete(T entity);
	Mono<Void> deleteAll(Iterable<? extends T> entities);
	Mono<Void> deleteAll(Publisher<? extends T> entityStream);
	Mono<Void> deleteAll();
}
```
위에서 언급했듯 기본적으로 제공하는 것 외에는 spring-data-jpa에서 엔티티의 필드로 연쇄적인 메소드 이름으로 쿼리를 생성하는 기능을 아직까지는 지원하지 않는다.     

그래서 필요한 것들은 직접 만들어야 한다. 뭐 어짜피 기본적인 CUD에 경우에는 가능하니 CustomRepository를 이용해서 작성해 볼까 한다.    

**User**

```
package io.basquiat.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
@Table("basquiat_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Builder
    public User(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Id
    @Column("id")
    private Long id;

    @Column("user_name")
    private String name;

    @Column("user_age")
    private int age;

}
``` 
 
 
**CustomUserRepository**

```
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
```
많은 것을 할 생각은 없다.     

차후 기능에 따라서 늘려나갈 수 있겠지만 이 인터페이스의 기능은 딱 4가지만 갖을 것이다. 
 
1. 기존 ReactiveCrudRepository에 있는 findAll()외에도 DatabaseClient를 이용한 조회 쿼리         

2. projection을 이용한 조회 쿼리        

3. insert user        

4. delete user       

**UserRepository**

```
package io.basquiat.user.repo;

import io.basquiat.user.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Long>, CustomUserRepository {

}
```

**UserRepositoryImpl**

```
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
```
커스텀 레파지토리를 작성할 때 해당 레파지토리를 구현하는 클래스 이름 마지막에는 Impl을 붙여야 한다.      

은근히 모르는 분들 많은데 이것은 spring-data에서 the-custom-interface-name-with-an-additional-Impl-suffix때문이다.    

basquiat_user table DDL

```
CREATE TABLE basquiat_user
(
    id serial NOT NULL,
	user_name character varying(255),
    user_age integer,
    CONSTRAINT basquiat_item_pkey PRIMARY KEY (id)
)
```
다음 스크립트를 실행해서 테이블을 하나 만든다.

**UserService**

```
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

}
```
Controller를 생성하기 전에 해당 서비스에 대한 코드가 생각한 대로 돌아가는지 테스트코드로 검증을 해봐야 한다.      

**UserServiceTest**

```
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

    @Test
    void testCustomDeleteUser() {
        Mono<Void> mono = userService.customDeleteUser(2L);
        StepVerifier.create(mono)
                    .verifyComplete();
    }

}
```
검증이 완료되면 콘트롤러를 작성하자.

**UserController**

```
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
```
가장 일반적인 방식이다. RouteFuntional방식도 좋긴 하지만 Swagger설정이 안되는거 같다.      

현재 이 프로젝트의 스웨거 주소는 다음과 간다.    

[localhost swagger](http://localhost:8080/documentation/swagger-ui/)    

그러면 이제 WebTestClient를 이용해 콘트롤러에 대한 테스트 코드 작성을 해보자.     

**UserControllerTest**

```
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

    @Test
    void testCustomDeleteUser() {
        webTestClient.delete()
                     .uri("/v1/user/{id}", 1L)
                     .exchange()
                     .expectStatus().isNoContent()
                     .expectBody().isEmpty();
    }

}
```

# At A Glance      
사실 비동기가 대세라고 무조건 WebFlux해야지 이런건 아닌거 같다.      

모든 것은 쓰임새가 있고 그에 맞춰서 잘 사용하는게 최고인거 같은데 일단 R2DBC는 모든 것을 완벽하게 운영에 사용하기엔 무리수가 있다.     

그리고 JPA를 생각했던 분이라면 모양만 비슷하지 완전 다르다.     

그래서 만일 정말 spring-data-jpa와 WebFlux를 써야한다면 그냥 fromCallable을 이용하는게 속 편하다.     

그리고 해당 프로젝트 issue를 살펴보면 애초에 이들은 JPA를 염두하고 있지도 않은 거 같다.     

특히 Lazy Loading과 relation에 대해서 그와 관련 장황하게 설명한 글이 있는데 그냥 느낌상으로는 그렇게구나 하는 생각이 든다.     

또한 Aggregation과 관련해서 좀 불편하다. 엔티티 내부에 컬렉션 타입을 지원하지 않는다.          

생각같아선 myBatis처럼 지원이라도 해주면 참 좋겠구먼.... mapper을 만들어야 하는게 큰 단점이다.          

그리고 몇 가지 converter도 제대로 지원하지 않아 커스터마이징을 해야하는 단계이다.          

어쨰든 이 레파지토리는 몇가지 주제를 가지고 개발을 해볼까 한다.      

여기서 테스트한 예제들은 차후 사용하지 않을 수 있다. 다만 어떤 방식으로 개발을 할 수 있는지, 테스트를 어떻게 해야 하는지에 대한 단초만 제공한다.     

p.s. 궁금한 점은 issue 또는 funnyjazz@naver.com으로 문의주세요.