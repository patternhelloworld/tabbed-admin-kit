package com.autofocus.pms.hq.domain.common.file.service;

import com.autofocus.pms.common.config.response.error.exception.data.ResourceNotFoundException;
import com.autofocus.pms.common.domain.file.bo.FileSystemStorageImpl;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.customer.dao.CustomerRepository;
import com.autofocus.pms.hq.domain.common.customer.entity.Customer;
import com.autofocus.pms.hq.domain.common.dept.dao.DeptRepository;
import com.autofocus.pms.hq.domain.common.dept.entity.Dept;
import com.autofocus.pms.hq.domain.common.file.dto.FileDTO;
import com.autofocus.pms.hq.domain.common.privacyagree.dao.PrivacyAgreeRepository;
import com.autofocus.pms.hq.domain.common.privacyagree.dto.PrivacyAgreeResDTO;
import com.autofocus.pms.hq.domain.common.user.dao.UserRepository;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class FileService {

    private final Environment environment;
    private final DeptRepository deptRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PrivacyAgreeRepository privacyAgreeRepository;

    private String makeDirectoryPath(String menuPath) {
        String directoryPath = "";
        if(menuPath.equals("depts")) {
            directoryPath = environment.getProperty("file.upload-dir.root.public.depts");
        } else if(menuPath.equals("customers")) {
            directoryPath = environment.getProperty("file.upload-dir.root.public.customers");
        }
        return directoryPath;
    }

    public FileDTO.TextEditor processDeptFile (String srcPath, final Integer idx, AccessTokenUserInfo accessTokenUserInfo, String sname) throws IOException {
        Dept dept = deptRepository.findById(idx).orElse(null);
        if(dept == null){
            throw new IllegalStateException("이미 등록되어 있는 조직의 이미지만 업로드가 가능합니다. 유효하지 않은 deptIdx : " + idx);
        }else{
            dept.setSelfImgFname(srcPath);
            deptRepository.save(dept);
        }

        return new FileDTO.TextEditor(srcPath+" : "+sname);
    }

    public FileDTO.TextEditor processCustomerFile (String srcPath, final Integer idx, AccessTokenUserInfo accessTokenUserInfo, String sname) throws IOException {
        Customer customer = customerRepository.findById(idx).orElse(null);

        if(customer == null){
            throw new IllegalStateException("이미 등록되어 있는 고객만 업로드가 가능합니다. 유효하지 않은 Idx : " + idx);
        }else{
            User user = userRepository.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()).orElseThrow(() -> new ResourceNotFoundException("User not found."));


        }

        return new FileDTO.TextEditor(srcPath+" : "+sname);
    }

    public FileDTO.TextEditor uploadFile (final String menuPath, MultipartFile file, UriComponentsBuilder uriComponentsBuilder, final Integer idx, AccessTokenUserInfo accessTokenUserInfo) throws IOException {
        String directoryPath = makeDirectoryPath(menuPath);

        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(directoryPath);
        String fileName = fileSystemStorageImpl.saveFile(file);
        String srcPath = uriComponentsBuilder.build().toUri().toString() + "/api/v1" + "/files/" + menuPath + "/" + fileName;

        if(menuPath.equals("depts")) {
            return processDeptFile(srcPath, idx, accessTokenUserInfo, file.getOriginalFilename());
        } else if(menuPath.equals("customers")) {
            return processCustomerFile(srcPath, idx, accessTokenUserInfo, file.getOriginalFilename());
        } else {
            throw new IllegalArgumentException("Invalid menuPath: " + menuPath);
        }
    }

    public FileDTO.TextEditor uploadNewFile (final String menuPath, MultipartFile file, UriComponentsBuilder uriComponentsBuilder) throws IOException {
        String directoryPath = makeDirectoryPath(menuPath);

        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(directoryPath);
        String fileName = fileSystemStorageImpl.saveFile(file);
        String imgSrcPath = uriComponentsBuilder
                .build().toUri().toString() + menuPath + "/files/" + fileName;

        return new FileDTO.TextEditor(imgSrcPath);
    }

    public ResponseEntity<byte[]> downloadBlobFile(final String menuPath, String fileName) throws IOException {

        String directoryPath = makeDirectoryPath(menuPath);

        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(directoryPath);

        Resource resource = fileSystemStorageImpl.loadFile(fileName);
        byte[] fileContent = StreamUtils.copyToByteArray(resource.getInputStream());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"blob\"");

        // ResponseEntity를 생성하여 반환합니다.
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileContent);
    }

    public ResponseEntity<Resource> downloadFile(final String menuPath, String fileName) throws IOException {
        String directoryPath = makeDirectoryPath(menuPath);

        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(directoryPath);

        Resource resource = fileSystemStorageImpl.loadFile(fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"my-file.jpg\"");

        // ResponseEntity를 생성하여 반환합니다.
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }

    public ResponseEntity<Resource> downloadFileByUrlLink(String encodedUrl) throws IOException {
        URL url = new URL(encodedUrl);

        // URL에서 스트림을 열고 임시 파일에 내용을 저장합니다.
        Path tempFile = Files.createTempFile("download", ".tmp");
        try (InputStream in = url.openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // 임시 파일에서 바이트 배열을 읽어서 ByteArrayResource에 저장합니다.
        byte[] fileContent = Files.readAllBytes(tempFile);
        ByteArrayResource resource = new ByteArrayResource(fileContent);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"my-file.jpg\"");

        // ResponseEntity를 생성하여 반환합니다.
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileContent.length)
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }
}
