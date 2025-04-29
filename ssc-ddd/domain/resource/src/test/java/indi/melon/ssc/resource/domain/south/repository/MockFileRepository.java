package indi.melon.ssc.resource.domain.south.repository;

import indi.melon.ssc.resource.domain.file.File;
import indi.melon.ssc.resource.domain.file.FileID;

/**
 * @author wangmenglong
 * @since 2025/4/3 14:41
 */
public class MockFileRepository extends MockRepository<FileID, File> implements FileRepository {
    @Override
    public File fileOf(FileID id) {
        return db.get(id);
    }

    @Override
    public File save(File file) {
        db.put(file.getId(), file);
        return file;
    }

    @Override
    public void delete(FileID id) {
        db.remove(id);
    }
}
