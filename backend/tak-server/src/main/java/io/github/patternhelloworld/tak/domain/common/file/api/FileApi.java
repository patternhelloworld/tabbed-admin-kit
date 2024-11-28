package io.github.patternhelloworld.tak.domain.common.file.api;

import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.file.dto.FileDTO;
import io.github.patternhelloworld.tak.domain.common.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileApi {
    private final FileService fileService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("files/{menuPath}/{idx}")
    public GlobalSuccessPayload<FileDTO.TextEditor> uploadExistingFile(@PathVariable("menuPath") final String menuPath,
                                                                           @PathVariable("idx") final Integer idx,
                                                                           @RequestParam("file") MultipartFile file,
                                                                           UriComponentsBuilder uriComponentsBuilder,
                                                                           @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) throws IOException {
        return new GlobalSuccessPayload<>(fileService.uploadFile(menuPath, file, uriComponentsBuilder, idx, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("files/{menuPath}/new")
    public GlobalSuccessPayload<FileDTO.TextEditor> uploadNewFile(@PathVariable("menuPath") final String menuPath, @RequestParam("file") MultipartFile file, UriComponentsBuilder uriComponentsBuilder) throws IOException {
        return new GlobalSuccessPayload<>(fileService.uploadNewFile(menuPath, file, uriComponentsBuilder));
    }
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/files/{menuPath}/{fileName}/blob")
    public ResponseEntity<byte[]> downloadBlobFile(@PathVariable("menuPath") final String menuPath, @PathVariable("fileName") String fileName) throws IOException {
        return fileService.downloadBlobFile(menuPath, fileName);
    }

    @GetMapping("/files/{menuPath}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("menuPath") final String menuPath, @PathVariable("fileName") String fileName) throws IOException {
        return fileService.downloadFile(menuPath, fileName);
    }
}
