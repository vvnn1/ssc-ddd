package com.github.vvnn1.domain.pojo;

import com.github.vvnn1.domain.entity.AssetId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author vvnn1
 * @since 2024/4/7 12:52
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class DeleteCommand {
    private Long id;
    private AssetId assetId;
}
