package com.parkchoi.scrum.util.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); // 파일 확장자
        if(!isValidExtensionImage(extension)){
            throw new ExtensionErrorException("이미지 확장자가 아닙니다.");
        }

        String randomName = UUID.randomUUID().toString(); // 랜덤한 문자열 생성
        String newFilename = "profile/" + randomName + extension; // 랜덤한 문자열과 확장자를 합쳐서 새 파일명 생성

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, newFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, newFilename).toString();
    }

    // 사진 확장자 검사
    private boolean isValidExtensionImage(String extension) {
        String[] allowedExtensions = {".jpg", ".jpeg", ".png"};
        for (String allowedExtension : allowedExtensions) {
            if (allowedExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    //사진 삭제
    public void deleteFile(String imageUrl){
        if(imageUrl == null || imageUrl.isEmpty()){
            return;
        }

        String key = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        amazonS3.deleteObject(bucket, key);
    }
}