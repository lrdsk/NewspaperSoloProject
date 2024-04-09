package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;

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
    @NotEmpty(message = "Place for photo should be not empty")
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
}
