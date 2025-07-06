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
//                             "resource_type", "image",
//                             "chunk_size", 6000000
//                     )
//             );
//             return uploadResult.get("secure_url").toString(); // URL of the uploaded image
//         } catch (Exception e) {
//             throw new IOException("Failed to upload image to Cloudinary: " + e.getMessage(), e);
//         }
//     }

//     // // Upload method for PDFs
//     // public String uploadPdf(MultipartFile file) throws IOException {
//     //     try {
//     //         Map uploadResult = cloudinary.uploader().upload(
//     //                 file.getBytes(),
//     //                 ObjectUtils.asMap(
//     //                         "resource_type", "raw", // Use "raw" for PDF files
//     //                         "chunk_size", 6000000
//     //                 )
//     //         );
//     //         return uploadResult.get("secure_url").toString(); // URL of the uploaded PDF
//     //     } catch (Exception e) {
//     //         throw new IOException("Failed to upload PDF to Cloudinary: " + e.getMessage(), e);
//     //     }
//     // }

//   public String uploadFile(MultipartFile file) throws IOException {
//     try {
//         Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
//             "resource_type", "auto"
//         ));
//         return uploadResult.get("secure_url").toString();
//     } catch (IOException e) {
//         throw new IOException("Failed to read file content", e);
//     } catch (Exception e) {
//         throw new IOException("Cloudinary upload failed", e);
//     }
// }
// }


















// // package com.Pahana_Edu_Backend.Cloudinary;

// // import java.io.IOException;
// // import java.util.Map;

// // import org.springframework.beans.factory.annotation.Value;
// // import org.springframework.stereotype.Service;
// // import org.springframework.web.multipart.MultipartFile;

// // import com.cloudinary.Cloudinary;
// // import com.cloudinary.utils.ObjectUtils;

// // @Service
// // public class CloudinaryService {

// //     private final Cloudinary cloudinary;

// //     // Inject config values from application.properties using @Value
// //     public CloudinaryService(
// //         @Value("${cloudinary.cloud-name}") String cloudName,
// //         @Value("${cloudinary.api-key}") String apiKey,
// //         @Value("${cloudinary.api-secret}") String apiSecret
// //     ) {
// //         this.cloudinary = new Cloudinary(ObjectUtils.asMap(
// //             "cloud_name", cloudName,
// //             "api_key", apiKey,
// //             "api_secret", apiSecret,
// //             "secure", true
// //         ));
// //     }

// //     // Upload method accepts a MultipartFile (uploaded file) and returns image URL
// //     public String uploadImage(MultipartFile file) throws IOException {
// //         Map uploadResult = cloudinary.uploader().upload(
// //             file.getBytes(),
// //             ObjectUtils.asMap(
// //               "resource_type", "image",
// //               "chunk_size", 6000000
// //             )
// //         );
// //         return uploadResult.get("secure_url").toString();  // URL of the uploaded image
// //     }
// // }

package com.Pahana_Edu_Backend.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret
    ) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "image"));
            return (String) uploadResult.get("url");
        } catch (Exception e) {
            log.error("Failed to upload image: {}", e.getMessage(), e);
            throw new IOException("Failed to upload image: " + e.getMessage());
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "raw"));
            return (String) uploadResult.get("url");
        } catch (Exception e) {
            log.error("Failed to upload file: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file: " + e.getMessage());
        }
    }
}