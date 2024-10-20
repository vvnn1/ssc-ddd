package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.common.exception.ApplicationInfrastructureException;
import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import indi.melon.ssc.draft.south.repository.dao.VersionDao;
import org.springframework.stereotype.Repository;

/**
 * @author wangmenglong
 * @since 2024/10/23 19:20
 */
@Repository
public class VersionRepositoryImpl implements VersionRepository {
    private final VersionDao versionDao;

    public VersionRepositoryImpl(VersionDao versionDao) {
        this.versionDao = versionDao;
    }

    @Override
    public Version versionOf(VersionID id) {
        try {
            return versionDao.findById(id).orElse(null);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("find version by " + id + " failed.", e);
        }
    }

    @Override
    public void delete(VersionID id) {
        try {
            versionDao.deleteById(id);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("delete version by " + id + " failed.", e);
        }
    }

    @Override
    public void save(Version version) {
        try {
            versionDao.save(version);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("save version failed. version: " + version, e);
        }
    }
}
