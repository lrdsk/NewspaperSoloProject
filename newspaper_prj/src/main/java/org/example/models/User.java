package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "name")
    @Size(min = 2, max = 100)
    @NotEmpty(message = "Name should be not empty")
    private String name;

    @Column(name = "surname")
    @Size(min = 2, max = 100)
    @NotEmpty(message = "Surname should be not empty")
    private String surname;

    @Column(name="email")
    @Size(max = 255)
    @NotEmpty(message = "Email should be not empty")
    @Email
    private String email;

    @Column(name="password")
    @NotEmpty(message = "Password should be not empty")
    private String password;

    @ManyToMany(mappedBy = "users", cascade = {CascadeType.MERGE})
    private Set<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(posts, user.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, surname, email, password);
    }
}
