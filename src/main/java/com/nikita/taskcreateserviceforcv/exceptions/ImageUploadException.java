package com.nikita.taskcreateserviceforcv.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageUploadException extends RuntimeException {

    public ImageUploadException(final String message) {
        super(message);
    }

}