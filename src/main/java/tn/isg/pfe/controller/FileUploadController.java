package tn.isg.pfe.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.isg.pfe.entities.Training;
import tn.isg.pfe.repository.TrainingRepo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    @Autowired
    TrainingRepo trainingRepo;

    private static final String UPLOAD_DIR = "C:\\Users\\User\\Documents\\pfee\\back\\PFE_learning\\src\\uploads\\";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("id") Long id) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }
        System.out.println(file.getName());
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);
            Training training = trainingRepo.findById(id).get();
            training.getAssignment().setFile_path(file.getOriginalFilename());
            trainingRepo.save(training);
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
}
