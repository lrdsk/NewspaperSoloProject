package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name="post")
public class Post {
    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @Column(name="photo")
    //@NotEmpty(message = "Place for photo should be not empty")
    private String photo;

    @Column(name="title")
    @NotEmpty(message = "Title should be not empty")
    @Size(max=255)
    private String title;

    @Column(name="information")
    @NotEmpty(message = "Information should be not empty")
    private String information;

    @Column(name = "date_publish")
    //@NotEmpty(message = "Date should be not empty")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePublish;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name="likes",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private Set<User> users;

    @ManyToMany/*(cascade = {CascadeType.PERSIST})*/
    @JoinTable(
            name="post_topic",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="topic_id")
    )
    private List<Topic> postTopicsList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return postId == post.postId && Objects.equals(photo, post.photo) && Objects.equals(title, post.title) && Objects.equals(information, post.information) && Objects.equals(datePublish, post.datePublish) && Objects.equals(users, post.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, title, information);
    }
}
