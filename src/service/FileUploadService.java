package service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.UUID;

@Service
public class FileUploadService {

    private final Path root = Paths.get("D:\\UTM\\Year3\\Semester1\\Internet Programming\\Codes\\ECO-JB\\WebContent\\files");
    
    public String saveFile(MultipartFile file) throws Exception {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root); // use createDirectories to create all nonexistent parent directories too
            }

            // Generate a unique name for the file
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            Path path = root.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), path);

            return path.toString();
        } catch (Exception e) {
            throw new Exception("Error saving file: " + e.getMessage());
        }
    }
}