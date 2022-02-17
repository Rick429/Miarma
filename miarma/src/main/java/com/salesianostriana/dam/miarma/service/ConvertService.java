package com.salesianostriana.dam.miarma.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ConvertService {

    File compressVideo(MultipartFile file);

    File scalrImage(MultipartFile file, int target);
}
