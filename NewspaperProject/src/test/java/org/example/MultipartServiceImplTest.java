package org.example;
import org.example.services.servicesImpl.MultipartServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MultipartServiceImplTest {

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private MultipartServiceImpl multipartService;

    private String testFileName = "test.jpg";

    @Before
    public void setup() {
        when(mockFile.getOriginalFilename()).thenReturn(testFileName);
        when(mockFile.getContentType()).thenReturn("image/jpeg");
    }

    @Test
    public void testSavePhoto() throws IOException {
        String savedFileName = multipartService.savePhoto(mockFile);
        assertEquals("attach_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")) + "_test.jpg", savedFileName);
    }

    @Test
    public void testLoadFileAsResource() throws MalformedURLException {
        String testFileName = "test.jpg";
        Resource resource = multipartService.loadFileAsResource(testFileName);
    }
}

