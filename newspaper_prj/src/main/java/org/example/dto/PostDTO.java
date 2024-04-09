package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private String photo;

    @NotEmpty(message = "Title should be not empty")
    @Size(max=255)
    private String title;

    @NotEmpty(message = "Information should be not empty")
    private String information;

    private Date datePublish;
}
