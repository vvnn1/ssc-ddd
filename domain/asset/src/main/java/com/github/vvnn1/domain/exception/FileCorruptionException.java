package com.github.vvnn1.domain.exception;

/**
 * @author vvnn1
 * @since 2024/4/7 20:44
 */
public class FileCorruptionException extends RuntimeException{
    public FileCorruptionException(String message) {
        super(message);
    }
}
