package org.example.servicesImpl;

import org.example.models.Post;
import org.example.models.User;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.example.services.LikeService;
import org.example.util.exceptions.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class LikeServiceImpl implements LikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private boolean isLiked = false;

    @Autowired
    public LikeServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public boolean setLikeToPost(String email, int post_id){// todo: добавить обработку исключений
        Post post = postRepository.findById(post_id).orElseThrow(PostNotFoundException::new);
        Set<User> users = post.getUsers();
        if(userRepository.findByEmail(email).isPresent()){
            User user = userRepository.findByEmail(email).get();
            if(users.contains(user)){
                users.remove(user);
                isLiked = false;
            }else {
                users.add(user);
                isLiked = true;
            }
        }

        post.setUsers(users);
        return isLiked;
    }

    @Override
    public int getCountLikes(int postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return post.getUsers().size();
    }
}
