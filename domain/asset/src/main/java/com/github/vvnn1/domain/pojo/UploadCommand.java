package com.github.vvnn1.domain.pojo;

import com.github.vvnn1.domain.entity.AssetID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.InputStream;

/**
 * @author vvnn1
 * @since 2024/4/7 11:12
 */
@Getter
@Setter
@ToString
public class UploadCommand {
    private AssetID assetId;
    private String fileName;
    private InputStream inputStream;
    private String identifier;
}
