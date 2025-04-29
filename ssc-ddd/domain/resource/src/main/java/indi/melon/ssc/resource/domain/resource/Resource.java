package indi.melon.ssc.resource.domain.resource;

import indi.melon.ssc.resource.domain.exception.FileNotMatchException;
import indi.melon.ssc.resource.domain.exception.IllegalRefCountException;
import indi.melon.ssc.resource.domain.file.File;
import indi.melon.ssc.resource.domain.file.FileID;
import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author wangmenglong
 * @since 2025/3/31 11:23
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class Resource {
    private ResourceID id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long refCount;
    private FileID fileID;

    Resource() {
    }

    public Resource(ResourceID id, String name, FileID fileID) {
        this.id = id;
        this.name = name;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.refCount = 0L;
        this.fileID = fileID;
    }

    public void incrRefCount(@Nonnull File file) {
        if (!fileID.equals(file.getId())) {
            throw new FileNotMatchException("oss file not match: " + file.getId());
        }

        refCount++;
        file.incRefCount();
    }

    public void decrRefCount(@Nonnull File file) {
        if (!fileID.equals(file.getId())) {
            throw new FileNotMatchException("oss file not match: " + file.getId());
        }

        if (refCount == 0) {
            throw new IllegalRefCountException("ref count is zero. can not decr any more.");
        }

        refCount--;
        file.decRefCount();
    }

    public void rename(String newName) {
        this.name = newName;
        this.updateTime = LocalDateTime.now();
    }
}
