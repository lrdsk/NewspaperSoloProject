package org.example.repositories;

import org.example.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT e FROM Post e ORDER BY e.datePublish DESC")
    List<Post> findAllByDateDesc();

    @Query("SELECT p\n" +
            "FROM Post p\n" +
            "JOIN p.postTopicsList pt\n" +
            "JOIN pt.selectedTopics t\n" +
            "WHERE t.user.id = :userId\n" +
            "AND t.status = 1\n" +
            "GROUP BY p\n" +
            "ORDER BY COUNT(DISTINCT t.id) DESC")
    List<Post> findPostsByUserId(@Param("userId") int userId);

    //@Query("SELECT p FROM Post p WHERE p.postId NOT IN (SELECT ust.id.topicId FROM UserSelectedTopic ust WHERE ust.status = 2)")
    @Query("SELECT p FROM Post p WHERE p.postId NOT IN (SELECT DISTINCT p.postId FROM Post p JOIN p.postTopicsList pt JOIN pt.selectedTopics st WHERE st.id.userId = :userId AND st.status = 2)")
    List<Post> findAllPostsExceptSelectedTopics(@Param("userId") int userId);
}
