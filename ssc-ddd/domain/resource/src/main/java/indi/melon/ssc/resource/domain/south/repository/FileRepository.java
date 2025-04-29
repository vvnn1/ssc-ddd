package indi.melon.ssc.resource.domain.south.repository;

import indi.melon.ssc.resource.domain.file.FileID;
import indi.melon.ssc.resource.domain.file.File;

/**
 * @author wangmenglong
 * @since 2025/4/1 21:54
 */
public interface FileRepository {
    File fileOf(FileID id);
    File save(File file);
    void delete(FileID id);
}
