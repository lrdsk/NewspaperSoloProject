package org.example.repositories;

import org.example.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT e FROM Post e ORDER BY e.datePublish DESC")
    List<Post> findAllByDateDesc();
}
