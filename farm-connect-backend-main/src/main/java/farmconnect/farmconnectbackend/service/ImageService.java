package farmconnect.farmconnectbackend.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    public List<String> uploadFile(List<MultipartFile> multipartFile) {
        List<String> fileNameList = new ArrayList<>();

        Bucket bucket = StorageClient.getInstance().bucket("farmconnect-app.appspot.com");
        multipartFile.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());

            try {
                InputStream content = new ByteArrayInputStream(file.getBytes());
                Blob blob = bucket.create(fileName, content, file.getContentType());

                fileNameList.add(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return fileNameList;
    }

    public Boolean deleteFile(String fileName) {
        Bucket bucket = StorageClient.getInstance().bucket("farmconnect-app.appspot.com");
        boolean result = bucket.get(fileName).delete();
        return result;
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}