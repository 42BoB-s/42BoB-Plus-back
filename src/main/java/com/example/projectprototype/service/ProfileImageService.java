package com.example.projectprototype.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileImageService {
    Long uploadProfile(MultipartFile file, String userId) throws IOException;
}
