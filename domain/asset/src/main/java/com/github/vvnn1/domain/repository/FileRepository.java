package com.github.vvnn1.domain.repository;

import com.github.vvnn1.domain.pojo.AssetFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author vvnn1
 * @since 2024/4/7 12:28
 */
public interface FileRepository {
    AssetFile storeFile(AssetFile file, InputStream is) throws IOException;
    void removeFile(String path);
}
