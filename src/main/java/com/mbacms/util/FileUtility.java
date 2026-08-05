package com.mbacms.util;

import com.mbacms.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

public class FileUtility {
    public static void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResourceNotFoundException("File is empty or not provided.");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            throw new ResourceNotFoundException("Only PDF documents are allowed.");
        }
    }
}
