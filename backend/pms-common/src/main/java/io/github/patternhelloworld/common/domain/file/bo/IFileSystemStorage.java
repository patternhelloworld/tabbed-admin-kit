package io.github.patternhelloworld.common.domain.file.bo;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileSystemStorage {

    String saveFile(MultipartFile file) throws IOException;

    Resource loadFile(String fileName);
}
