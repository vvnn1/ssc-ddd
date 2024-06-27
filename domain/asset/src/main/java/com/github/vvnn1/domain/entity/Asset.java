package com.github.vvnn1.domain.entity;

import com.github.vvnn1.cqrs.AbstractAggregateRoot;
import com.github.vvnn1.domain.exception.FileCorruptionException;
import com.github.vvnn1.domain.pojo.*;
import com.github.vvnn1.domain.repository.FileRepository;
import com.github.vvnn1.utils.StringUtils;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author vvnn1
 * @since 2024/4/6 16:42
 */
@Getter
@Setter
@ToString
@Entity
public class Asset extends AbstractAggregateRoot {
    @EmbeddedId
    private AssetID id;
    private Integer refCount;
    private String path;
    private String identifier;
    private static final String EMPTY_PATH = "";

    public void handle(UploadCommand command, FileRepository fileRepository) throws IOException {
        if (!hasUpload()) {
            AssetFile af = fileRepository.storeFile(
                    new AssetFile(
                            command.getFileName(),
                            command.getIdentifier(),
                            EMPTY_PATH
                    ),
                    command.getInputStream()
            );

            if (!StringUtils.equals(identifier, af.getIdentifier())) {
                throw new FileCorruptionException("文件唯一标识不一致，文件可能损坏");
            }
            path = af.getPath();
        }

        refCount++;
        addEvent(
                new UploadedEvent(
                        id,
                        command.getFileName(),
                        path,
                        identifier,
                        LocalDateTime.now()
                )
        );
    }

    public void handle(DeleteCommand command, FileRepository fileRepository) {
        if (!hasUpload()) {
            return;
        }

        refCount--;
        if (refCount == 0) {
            fileRepository.removeFile(path);
            path = EMPTY_PATH;
        }
        addEvent(
                new DeletedEvent(
                        command.getId()
                )
        );
    }

    private boolean hasUpload() {
        return path.compareTo(EMPTY_PATH) != 0 && refCount > 0;
    }
}
