package indi.melon.ssc.resource.south.repository;

import indi.melon.ssc.common.exception.ApplicationInfrastructureException;
import indi.melon.ssc.resource.domain.file.File;
import indi.melon.ssc.resource.domain.file.FileID;
import indi.melon.ssc.resource.domain.south.repository.FileRepository;
import indi.melon.ssc.resource.south.repository.dao.FileDao;
import org.springframework.stereotype.Repository;

/**
 * @author wangmenglong
 * @since 2025/4/21 21:19
 */
@Repository
public class FileRepositoryImpl implements FileRepository {
    private final FileDao fileDao;

    public FileRepositoryImpl(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    @Override
    public File fileOf(FileID id) {
        try {
            return fileDao.findById(id).orElse(null);
        } catch (Exception e) {
            throw new ApplicationInfrastructureException("find file by " + id + " failed.", e);
        }
    }

    @Override
    public File save(File file) {
        try {
            return fileDao.save(file);
        } catch (Exception e) {
            throw new ApplicationInfrastructureException("save file failed. file: " + file, e);
        }
    }

    @Override
    public void delete(FileID id) {
        try {
            fileDao.deleteById(id);
        } catch (Exception e) {
            throw new ApplicationInfrastructureException("delete file by " + id + " failed.", e);
        }
    }
}
