package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import indi.melon.ssc.draft.south.repository.dao.VersionDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static indi.melon.ssc.draft.domain.draft.VersionBuildUtil.buildVersion;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author wangmenglong
 * @since 2024/10/23 19:20
 */
@DataJpaTest(
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {VersionRepository.class, VersionDao.class}
                )
        }
)
public class VersionRepositoryIT {
    @Autowired
    private VersionRepository versionRepository;
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void should_save_and_get_same_version() {
        Version version = buildVersion();

        versionRepository.save(version);

        Version versionDB = versionRepository.versionOf(new VersionID("VersionID1"));
        assertEquals(version.getId(), versionDB.getId());
        assertEquals(version.getContent(), versionDB.getContent());
        assertEquals(version.getCreator(), versionDB.getCreator());
//        assertEquals(version.getCreateTime(), versionDB.getCreateTime());
        assertEquals(version.getRemark(), versionDB.getRemark());
        assertEquals(version.getDraftID(), versionDB.getDraftID());
        assertEquals(version.getLocked(), versionDB.getLocked());
    }

}
