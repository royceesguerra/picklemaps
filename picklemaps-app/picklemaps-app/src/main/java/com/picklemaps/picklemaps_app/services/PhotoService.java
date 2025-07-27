package com.picklemaps.picklemaps_app.services;

import com.picklemaps.picklemaps_app.domain.entities.Photo;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PhotoService {

    Photo uploadPhoto(MultipartFile file);

    Optional<Resource> getPhotoAsResource(String id);
}
