package indi.melon.ssc.resource.domain.file;

import lombok.Getter;

/**
 * @author wangmenglong
 * @since 2025/3/31 14:55
 */
@Getter
public class File {
    private FileID id;
    private Long refCount;
    private String url;
    private Long version;

    public File(FileID id, String url) {
        this.id = id;
        this.refCount = 0L;
        this.url = url;
    }

    public void incRefCount() {
        this.refCount++;
    }

    public void decRefCount() {
        this.refCount--;
    }
}
