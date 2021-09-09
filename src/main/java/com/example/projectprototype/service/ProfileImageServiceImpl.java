package com.example.projectprototype.service;

import com.example.projectprototype.entity.User;
import com.example.projectprototype.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {

    @Value("${image.dir}")
    private String imgDir;
    private final UserRepository userRepository;

    public Long uploadProfile(MultipartFile file, String userId) throws IOException {
        if (file.isEmpty())
            return -14L; // 넘겨받은 file 이 없으면 오류

        long check = typeCheck(file); // 파일 유형 체크
        if (check < 0L)
            return check;
        String newFileName = createFileName(file.getOriginalFilename()); // 파일 이름 변형
        check = deleteOldPicture(userId); // 기존 파일 삭제, 삭제를 시도했으나 실패하면 에러
        if (check < 0L)
            return check;
        file.transferTo(new File(imgDir + newFileName)); // 파일 저장
        userRepository.updateProfilePath(newFileName, userId); // 새로운 파일 이름 DB update, uuid 만 추가됨
        return 1L;
    }

    private Long typeCheck(MultipartFile file) {
        List<String> allowedExt = Arrays.asList("png","jpg","jpeg","bmp","PNG","JPG","JPEG","BMP");
        String nameExt = extractNameExt(file.getOriginalFilename());
        String contentExt = extractContentExt(file.getContentType());

        if (!allowedExt.contains(nameExt) || !allowedExt.contains(contentExt))
            return -11L; // 파일 확장자와 contentType 이 png, jpg, jpeg, BMP 가 아니면 오류
        if (!nameExt.equals(contentExt))
            return -12L;  // 파일 확장자와 contentType 이 일치하지 않으면 오류
        return 1L;
    }

    private Long deleteOldPicture(String userId) {
        User user = userRepository.findById(userId).get();
        if (!isIntraPicture(user)) {
            System.out.println("not intra picture");
            File file = new File(user.getProfile());
            if (file.delete())
                return 2L; // 파일 삭제를 시도했고, 성공하였음.
            else
                return -13L; // 파일 삭제 시도했으나, 실패한 에러
        }
        return 1L; // 인트라 프로필을 사용중이어서 사진을 삭제할 필요가 없음.
    }

    private String createFileName(String originalFilename) {
        String ext = extractNameExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 현재 DB 에 저장되어 있는 profile 이 인트라 사진 경로인지 체크
    private Boolean isIntraPicture(User user) {
        String intraPictureUrl = "https://cdn.intra.42.fr/users/"+ user.getId() +".jpg";
        return user.getProfile().equals(intraPictureUrl);
    }

    // 파일 명에서 확장자 추출
    private String extractNameExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    // contentType 에서 확장자 추출
    private String extractContentExt(String contentType) {
        int pos = contentType.lastIndexOf("/");
        return contentType.substring(pos + 1);
    }
}
