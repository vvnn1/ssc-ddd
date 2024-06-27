package com.github.vvnn1.domain.service;

import com.github.vvnn1.domain.pojo.DeleteCommand;
import com.github.vvnn1.domain.pojo.UploadCommand;

import java.io.IOException;

/**
 * @author vvnn1
 * @since 2024/4/6 22:22
 */
public interface AssetService {
    void uploadFile(UploadCommand command) throws IOException;
    void deleteFile(DeleteCommand command);
}
