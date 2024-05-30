package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="topic")
@Data
@NoArgsConstructor
public class Topic {

    @Id
    @Column(name="topic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int topicId;

    @Column(name="name")
    @Size(max = 255)
    private String name;

    @ManyToMany(mappedBy = "topics", cascade = {CascadeType.MERGE})
    private List<User> users;

    @ManyToMany(mappedBy = "postTopicsList", cascade = {CascadeType.MERGE})
    private List<Post> posts;

}
