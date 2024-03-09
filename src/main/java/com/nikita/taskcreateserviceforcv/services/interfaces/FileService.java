package com.nikita.taskcreateserviceforcv.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public String upload(final MultipartFile file);

}
