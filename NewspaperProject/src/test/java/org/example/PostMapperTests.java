package org.example;

import static org.junit.Assert.assertEquals;
import org.example.dto.PostDTO;
import org.example.models.Post;
import org.example.util.mappers.PostMapper;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.Date;

public class PostMapperTests {

    private ModelMapper modelMapper = new ModelMapper(); // Создание реального объекта

    private PostMapper postMapper = new PostMapper(modelMapper); // Передача реального объекта в PostMapper

    @Test
    public void testToEntity() {
        PostDTO postDTO = new PostDTO(1, "sample_photo.jpg", "Test Title", "Testing Information", new Date());

        Post post = postMapper.toEntity(postDTO);

        assertEquals("Test Title", post.getTitle());
    }

    @Test
    public void testToDto() {
        // Создаем тестовые данные
        Post post = new Post();
        post.setPostId(1);
        post.setTitle("Test Title");
        post.setInformation("Testing Information");
        post.setDatePublish(new Date());

        PostDTO postDTO = postMapper.toDto(post);

        assertEquals("Test Title", postDTO.getTitle());
    }
}
