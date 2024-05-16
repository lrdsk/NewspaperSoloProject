package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDTO {

    @Size(max=1000)
    @NotEmpty(message = "Information should be not empty")
    private String info;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotEmpty
    private Date datePublish;

    @JsonIgnore
    private String userEmail;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotEmpty(message = "Name should be not empty")
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotEmpty(message = "Surname should be not empty")
    private String surname;

    @JsonIgnore
    private Integer postId;

}
