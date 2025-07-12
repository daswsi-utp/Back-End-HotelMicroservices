package com.microservice_rooms.controllers;

import com.microservice_rooms.service.IS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {
    @Value("${spring.destination.folder}")
    private String destinationFolder;
    @Autowired
    private IS3Service is3Service;

   @PostMapping("/createImage")
    public ResponseEntity<String> createBucket(@RequestParam String bucketName){
      return ResponseEntity.ok(is3Service.createBucket(bucketName));
   }
   @GetMapping("/check/{bucketName}")
    public ResponseEntity<String> checkBucket(@PathVariable String bucketName){
       return ResponseEntity.ok(is3Service.checkIfBucketExists(bucketName));
   }
   @GetMapping("/list")
   public ResponseEntity<List<String>> listBuckets(){
       return ResponseEntity.ok(is3Service.getAllBuckets());
   }
   @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam  String bucketName, @RequestParam String key, @RequestParam MultipartFile file) throws IOException {
       try{
           Path staticDir = Paths.get(destinationFolder);
           if(!Files.exists(staticDir)){
               Files.createDirectories(staticDir);
           }
           Path filePath =  staticDir.resolve(file.getOriginalFilename());
           Path finalPath = Files.write(filePath, file.getBytes());
           Boolean result = this.is3Service.uploadFile(bucketName, key, finalPath);
           if(result){
               Files.delete(finalPath);
               return ResponseEntity.ok("File was uploaded");
           } else {
               return ResponseEntity.internalServerError().body("File couldn't be uploaded to the bucket");
           }
       }catch (IOException e){
            throw new IOException("File couldn't be processed");
       }
   }
   @GetMapping("/download")
    public ResponseEntity<String> downloadFile(String bucketName, String key) throws IOException{
       this.is3Service.downloadFile(bucketName,key);
       return ResponseEntity.ok("File was downloaded correctly");
   }
   @PostMapping("/upload/presigned")
   public ResponseEntity<String> generatePresignedUploadUrl(@RequestParam String bucketName, @RequestParam String key, @RequestParam Long time){
       Duration durationToLive = Duration.ofMinutes(time);
       return ResponseEntity.ok(this.is3Service.generatePreSignedUpUploadUrl(bucketName, key, durationToLive));
   }
   @GetMapping("/download/presigned")
    public ResponseEntity<String> generatePresignedDownlaodUrl(@RequestParam String bucketName, @RequestParam String key, @RequestParam Long time){
        Duration durationToLive = Duration.ofMinutes(time);
        return ResponseEntity.ok(this.is3Service.generatePreSignedUpDownloadUrl(bucketName, key, durationToLive));
    }
}
