package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_selected_topic")
@Data
public class UserSelectedTopic {

    @EmbeddedId
    private UserSelectedTopicId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("selectedTopics")
    User user;

    @ManyToOne
    @MapsId("topicId")
    @JoinColumn(name = "topic_id")
    @JsonIgnoreProperties("selectedTopics")
    Topic topic;

    private int status;

    // геттеры и сеттеры

    @Override
    public String toString() {
        return "UserSelectedTopic{" +
                "id=" + id +
                ", topic=" + topic +
                ", status=" + status +
                '}';
    }
    @Override
    public int hashCode() {
        return Objects.hash(user.getUserId(), topic.getTopicId());
    }


}
