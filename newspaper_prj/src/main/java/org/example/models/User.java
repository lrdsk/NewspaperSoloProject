package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@Table(name="user")
public class User {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "name")
    @Size(min = 2, max = 100)
    @NotNull
    private String name;

    @Column(name = "surname")
    @Size(min = 2, max = 100)
    @NotNull
    private String surname;

    @Column(name="email")
    @Size(max = 255)
    @NotNull
    @Email
    private String email;

    @Column(name="password")
    @NotNull
    private String password;
}
