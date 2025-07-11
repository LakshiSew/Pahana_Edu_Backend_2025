// package com.Pahana_Edu_Backend.Cloudinary;

// import com.cloudinary.Cloudinary;
// import com.cloudinary.utils.ObjectUtils;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.IOException;
// import java.util.Map;

// @Service
// public class CloudinaryService {

//     private final Cloudinary cloudinary;

//     // Inject config values from application.properties using @Value
//     public CloudinaryService(
//             @Value("${cloudinary.cloud-name}") String cloudName,
//             @Value("${cloudinary.api-key}") String apiKey,
//             @Value("${cloudinary.api-secret}") String apiSecret
//     ) {
//         this.cloudinary = new Cloudinary(ObjectUtils.asMap(
//                 "cloud_name", cloudName,
//                 "api_key", apiKey,
//                 "api_secret", apiSecret,
//                 "secure", true
//         ));
//     }

//     // Upload method for images
//     public String uploadImage(MultipartFile file) throws IOException {
//         try {
//             Map uploadResult = cloudinary.uploader().upload(
//                     file.getBytes(),
//                     ObjectUtils.asMap(
//                             "resource_type", "auto",
//                             "chunk_size", 6000000
//                     )
//             );
//             return uploadResult.get("secure_url").toString(); // URL of the uploaded image
//         } catch (Exception e) {
//             throw new IOException("Failed to upload image to Cloudinary: " + e.getMessage(), e);
//         }
//     }

// }

package com.Pahana_Edu_Backend.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file, String resourceType) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        Map<String, Object> uploadResult = cloudinary.uploader()
            .upload(file.getBytes(), 
                ObjectUtils.asMap(
                    "resource_type", resourceType,
                    "folder", "pahana_edu" // Optional: Organize files in Cloudinary
                )
            );

        return uploadResult.get("secure_url").toString();
    }

    // Helper methods for specific file types
    public String uploadImage(MultipartFile image) throws IOException {
        return uploadFile(image, "image");
    }

      public String uploadPdf(MultipartFile pdf) throws IOException {
        if (pdf.getSize() > 10_000_000) {
            throw new IllegalArgumentException("PDF file size exceeds 10MB limit");
        }
        return uploadFile(pdf, "raw");
    }
}

















