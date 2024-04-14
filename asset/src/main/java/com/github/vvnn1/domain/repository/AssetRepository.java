package com.github.vvnn1.domain.repository;

import com.github.vvnn1.domain.entity.Asset;
import com.github.vvnn1.domain.entity.AssetID;

/**
 * @author vvnn1
 * @since 2024/4/7 17:03
 */
public interface AssetRepository {
    Asset findById(AssetID id);
    void save(Asset asset);
}
