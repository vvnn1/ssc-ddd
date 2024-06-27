package com.github.vvnn1.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author vvnn1
 * @since 2024/4/7 12:29
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class AssetFile {
    private String fileName;
    private String identifier;
    private String path;
}
