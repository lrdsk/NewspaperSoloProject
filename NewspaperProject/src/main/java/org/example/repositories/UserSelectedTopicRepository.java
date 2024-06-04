package org.example.repositories;

import org.example.models.UserSelectedTopic;
import org.example.models.UserSelectedTopicId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSelectedTopicRepository extends JpaRepository<UserSelectedTopic, UserSelectedTopicId> {
}

