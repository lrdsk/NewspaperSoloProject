package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Size(min = 2, max = 100)
    @NotEmpty(message = "Name should be not empty")
    private String name;

    @Size(min = 2, max = 100)
    @NotEmpty(message = "Surname should be not empty")
    private String surname;

    @Size(max = 255)
    @NotEmpty(message = "Email should be not empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should be not empty")
    private String password;
}
