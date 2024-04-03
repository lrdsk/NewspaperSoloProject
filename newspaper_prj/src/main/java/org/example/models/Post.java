package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name="post")
public class Post {
    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @Column(name="title")
    @NotNull
    @Size(max=255)
    private String title;

    @Column(name="information")
    @NotNull
    private String information;

    @Column(name = "date_publish")
    @NotNull
    private Timestamp datePublish;
}
