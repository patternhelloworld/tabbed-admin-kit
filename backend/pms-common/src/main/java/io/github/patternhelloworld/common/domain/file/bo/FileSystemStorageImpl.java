package io.github.patternhelloworld.common.domain.file.bo;

import io.github.patternhelloworld.common.config.response.error.exception.file.FileNotFoundException;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/*
*   README.md 의 파일 경로 변수명 규약 확인
* */
public class FileSystemStorageImpl implements IFileSystemStorage {

    private Path absoluteDirectoryPath;
    private String relativeDirectoryPathStr;

    public FileSystemStorageImpl(String relativeDirectoryPathStr) throws IOException {
        this.absoluteDirectoryPath = Paths.get(relativeDirectoryPathStr)
                .toAbsolutePath()
                .normalize();
        this.relativeDirectoryPathStr = relativeDirectoryPathStr;

        Files.createDirectories(this.absoluteDirectoryPath);
    }

    public String saveFile(@NotNull MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "." + originalFilename.substring(originalFilename.lastIndexOf(".")+1);

        Path fullFilePath = this.absoluteDirectoryPath.resolve(fileName);
        Files.copy(file.getInputStream(), fullFilePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public String saveFile(@NotNull MultipartFile file, String fileNameWithoutExt) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String fileName = fileNameWithoutExt + "." + originalFilename.substring(originalFilename.lastIndexOf(".")+1);

        Path fullFilePath = this.absoluteDirectoryPath.resolve(fileName);
        Files.copy(file.getInputStream(), fullFilePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }


    public FileSystemStorageImpl(){

    }
    public Resource loadFile(String fileName) {

        Path file = this.absoluteDirectoryPath.resolve(fileName).normalize();

        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("해당 파일을 찾을 수 없습니다. (" + fileName  + ")");
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("해당 파일을 다운로드 할 수 없습니다. (" + fileName  + ")");
        }
    }
    public long getFileSize(String fileName) throws IOException {
        Path file = this.absoluteDirectoryPath.resolve(fileName).normalize();
        if (Files.exists(file) && Files.isReadable(file)) {
            return Files.size(file);
        } else {
            throw new IOException("파일이 존재하지 않거나 읽을 수 없습니다: " + fileName);
        }
    }
}
