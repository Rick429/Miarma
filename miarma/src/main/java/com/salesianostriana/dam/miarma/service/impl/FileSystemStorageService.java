package com.salesianostriana.dam.miarma.service.impl;

import com.salesianostriana.dam.miarma.config.StorageProperties;
import com.salesianostriana.dam.miarma.errors.exception.FileNotFoundException;
import com.salesianostriana.dam.miarma.errors.exception.StorageException;
import com.salesianostriana.dam.miarma.service.StorageService;
import com.salesianostriana.dam.miarma.utils.MediaTypeUrlResource;
import lombok.extern.java.Log;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
@Log
@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("No se pudo inicializar la ubicación de almacenamiento", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String newFilename = "";
        try {
            if (file.isEmpty()) {
                throw new StorageException("El fichero stá vacío");
            }
            newFilename = filename;
            while (Files.exists(rootLocation.resolve(newFilename))) {
                String extension = StringUtils.getFilenameExtension(newFilename);
                String name = newFilename.replace("." + extension, "");
                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length() - 6);

                newFilename = name + "_" + suffix + "." + extension;
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, rootLocation.resolve(newFilename)
                        , StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new StorageException("Error al guardar el fichero: " + newFilename, ex);
        }
        return newFilename;
    }

    public String resizeStore(File file) {
        String filename = StringUtils.cleanPath(file.getName());
        String newFilename = "";
        try {
            if (file == null)
                throw new StorageException("El fichero subido está vacío");

            newFilename = filename;
            while (Files.exists(rootLocation.resolve(newFilename))) {
                String extension = StringUtils.getFilenameExtension(newFilename);
                String name = newFilename.replace("." + extension, "");
                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length() - 6);
                newFilename = name + "_" + suffix + "." + extension;

            }
            InputStream input = new FileInputStream(file);
            try (InputStream inputStream = input) {
                Files.copy(inputStream, rootLocation.resolve(newFilename),
                        StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException ex) {
            throw new StorageException("Error en el almacenamiento del fichero: " + newFilename, ex);
        }

        return newFilename;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException ex) {
            throw new StorageException("Error al leer los ficheros almacenados", ex);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            MediaTypeUrlResource resource = new MediaTypeUrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(
                        "No se pudo leer el archivo: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("No se pudo leer el archivo: " + filename, e);
        }
    }

    @Override
    public void deleteFile(String filename) {

    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public String uploadImage(MultipartFile file) {
        String filename = store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
        return uri;
    }

    @Override
    public String uploadResizeImage(MultipartFile file, int target) {
        try {

            byte[] byteImg = Files.readAllBytes(Paths.get(file.getOriginalFilename()));
            BufferedImage original = ImageIO.read(
                    new ByteArrayInputStream(byteImg)
            );

            BufferedImage scaled = Scalr.resize(original, target);

            File f1 = new File("temp",file.getOriginalFilename());

            ImageIO.write(scaled, "jpg", f1);

            String filenameThumbnail = resizeStore(f1);
            
            String uriThumb = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(filenameThumbnail)
                    .toUriString();
            return uriThumb;
        } catch (IOException ex) {
            throw new StorageException("Error al leer los ficheros almacenados", ex);
        }
    }
}
