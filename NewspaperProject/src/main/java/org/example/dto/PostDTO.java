package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int postId;

    @JsonIgnore
    private String photo;

    @NotEmpty(message = "Title should be not empty")
    @Size(max=255)
    private String title;

    @NotEmpty(message = "Information should be not empty")
    private String information;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date datePublish;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<TopicDTO> topicDTOList;
}
