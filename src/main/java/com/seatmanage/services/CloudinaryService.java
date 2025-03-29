package com.seatmanage.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    public String uploadImage(MultipartFile image) throws IOException {
        String uploadDir = new File("uploads").getAbsolutePath() + File.separator;
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs(); // Tạo thư mục nếu chưa tồn tại

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        File destFile = new File(uploadDir + fileName);
        image.transferTo(destFile); // Lưu file

        String serverUrl = "http://localhost:8080/uploads/";
        String imageUrl = serverUrl + fileName;
        return imageUrl;
    }
}
