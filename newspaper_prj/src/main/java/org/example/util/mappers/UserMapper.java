package org.example.util.mappers;

import org.example.dto.PostDTO;
import org.example.dto.UserDTO;
import org.example.models.Post;
import org.example.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toEntity(UserDTO userDTO){
        return Objects.isNull(userDTO) ? null : modelMapper.map(userDTO, User.class);
    }

    public UserDTO toDto(User user){
        return Objects.isNull(user) ? null : modelMapper.map(user, UserDTO.class);
    }
}
