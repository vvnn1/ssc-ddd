package indi.melon.ssc.resource.south.repository;

import indi.melon.ssc.resource.domain.file.File;
import indi.melon.ssc.resource.domain.file.FileID;
import indi.melon.ssc.resource.domain.south.repository.FileRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangmenglong
 * @since 2025/4/23 14:19
 */
public class MockFileRepository implements FileRepository {
    private final Map<FileID, File> db = new HashMap<>();

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
