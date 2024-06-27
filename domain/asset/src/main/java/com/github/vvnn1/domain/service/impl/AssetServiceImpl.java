package com.github.vvnn1.domain.service.impl;

import com.github.vvnn1.domain.entity.Asset;
import com.github.vvnn1.domain.pojo.DeleteCommand;
import com.github.vvnn1.domain.pojo.UploadCommand;
import com.github.vvnn1.domain.repository.AssetRepository;
import com.github.vvnn1.domain.repository.FileRepository;
import com.github.vvnn1.domain.service.AssetService;
import lombok.AllArgsConstructor;

import java.io.IOException;

/**
 * @author vvnn1
 * @since 2024/4/6 22:22
 */
@AllArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final FileRepository fileRepository;


    @Override
    public void uploadFile(UploadCommand command) throws IOException {
        Asset asset = assetRepository.findById(command.getAssetId());
        asset.handle(command, fileRepository);
        assetRepository.save(asset);
    }

    @Override
    public void deleteFile(DeleteCommand command) {
        Asset asset = assetRepository.findById(command.getAssetId());
        asset.handle(command, fileRepository);
        assetRepository.save(asset);
    }
}
