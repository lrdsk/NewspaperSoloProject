package org.example.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/photo")
public class MyController {
    private final MediaType mediaType = MediaType.MULTIPART_FORM_DATA;
    @GetMapping("/1")
    public ResponseEntity<String> getPictures(){
        return ResponseEntity.ok("Picture number 1");
    }

    @PostMapping(value = "/paste")
    public ResponseEntity<String> uploadPhoto(@RequestParam("photoFile") MultipartFile photoFile){
        try (InputStream is = photoFile.getInputStream()) {
            return  ResponseEntity.ok("Name:" + photoFile.getName()
                    + " File Name:" + photoFile.getOriginalFilename() + ", Size:" + is.available());
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
