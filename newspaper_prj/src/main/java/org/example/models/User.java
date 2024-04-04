package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
}
