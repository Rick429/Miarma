package com.salesianostriana.dam.miarma.service;

import io.github.techgnious.exception.VideoException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    String resizeStore(File file);

    String compressVideo(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteFile(String filename);

    void deleteAll();

    String uploadImage(MultipartFile file);

    String uploadResizeImage(MultipartFile file, int target);
}