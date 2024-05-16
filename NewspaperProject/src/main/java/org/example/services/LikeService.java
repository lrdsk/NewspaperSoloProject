package org.example.services;

public interface LikeService {
    boolean setLikeToPost(String email, int postId);
    int getCountLikes(int postId);
}
