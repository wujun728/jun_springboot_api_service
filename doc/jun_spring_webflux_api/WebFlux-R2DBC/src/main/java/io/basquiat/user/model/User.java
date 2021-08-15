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
