package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class UserSelectedTopicId implements Serializable {

    @Column(name = "user_id")
    private int userId;

    @Column(name = "topic_id")
    private int topicId;

    public UserSelectedTopicId(int userId, int topicId) {
        this.userId = userId;
        this.topicId = topicId;
    }

}
