package com.salesianostriana.dam.miarma.service.impl;

import com.salesianostriana.dam.miarma.config.StorageProperties;
import com.salesianostriana.dam.miarma.errors.exception.FileNotFoundException;
import com.salesianostriana.dam.miarma.errors.exception.StorageException;
import com.salesianostriana.dam.miarma.service.ConvertService;
import com.salesianostriana.dam.miarma.service.StorageService;
import com.salesianostriana.dam.miarma.utils.MediaTypeUrlResource;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
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
    private final ConvertService convertService;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, ConvertService convertService) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.convertService = convertService;
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
                throw new StorageException("El fichero está vacío");
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

    public String fileStore(File file) {
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
        try {
            String lista[] = filename.split("/");
            String name = lista[lista.length - 1];
            Path p = Paths.get(String.valueOf(rootLocation), name);
            Files.deleteIfExists(p);
        } catch (IOException e) {
            throw new FileNotFoundException("No se pudo encontrar el archivo: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public String uploadImage(MultipartFile file) {
        String filename = store(file);
        return completeUri(filename);
    }

    @Override
    public String uploadResizeImage(MultipartFile file, int target) {

        File imageFile = convertService.scalrImage(file, target);
        String filenameThumbnail = fileStore(imageFile);

            Path p = Paths.get( "temp", imageFile.getName());
        try {
            Files.deleteIfExists(p);
        } catch (IOException e) {
            throw new StorageException("No se pudo eliminar el fichero", e);
        }
        return completeUri(filenameThumbnail);

    }

    @Override
    public String uploadVideo(MultipartFile file) {

        File videoFile = convertService.compressVideo(file);
        String videoname = fileStore(videoFile);

        Path p = Paths.get("temp", videoFile.getName());
        try {
            Files.deleteIfExists(p);
        } catch (IOException e) {
            throw new StorageException("No se pudo eliminar el fichero", e);
        }
        return completeUri(videoname);
    }

    private String completeUri(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();
    }
}
