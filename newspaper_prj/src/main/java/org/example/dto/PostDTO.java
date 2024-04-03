package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    @NotNull
    @Size(max=255)
    private String title;

    @NotNull
    private String information;

    @NotNull
    private Timestamp datePublish;
}
