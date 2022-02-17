package com.salesianostriana.dam.miarma.service.impl;

import com.salesianostriana.dam.miarma.errors.exception.StorageException;
import com.salesianostriana.dam.miarma.service.ConvertService;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ConvertServiceImpl implements ConvertService {

    @Override
    public File compressVideo(MultipartFile file) {

        byte[] video;
        File videoFile;
        IVCompressor compressor = new IVCompressor();
        try {
            video = compressor.reduceVideoSize(file.getBytes(), VideoFormats.MP4, ResizeResolution.R480P);

            videoFile = new File("temp", file.getOriginalFilename());
            FileOutputStream fos = null;

            fos = new FileOutputStream(videoFile);
            fos.write(video);
            fos.close();
        } catch (VideoException | IOException e) {
            throw new StorageException("Error al leer el fichero", e);
        }
        return videoFile;
    }

    @Override
    public File scalrImage(MultipartFile file, int target) {
        File imagenFile;
        try {
            byte[] byteImg = Files.readAllBytes(Paths.get(file.getOriginalFilename()));
            BufferedImage original = ImageIO.read(
                    new ByteArrayInputStream(byteImg)
            );

            BufferedImage scaled = Scalr.resize(original, target);

            imagenFile = new File("temp", file.getOriginalFilename());

            ImageIO.write(scaled, "jpg", imagenFile);

        } catch (IOException ex) {
            throw new StorageException("Error al leer los ficheros almacenados", ex);
        }
        return imagenFile;
    }


}
