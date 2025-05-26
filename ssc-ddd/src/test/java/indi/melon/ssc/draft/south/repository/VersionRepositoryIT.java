package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import indi.melon.ssc.draft.south.repository.dao.VersionDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static indi.melon.ssc.draft.domain.draft.VersionBuildUtil.buildVersion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void should_save_and_get_same_version() {
        LocalDateTime beforeCreateTime = LocalDateTime.now();
        Version version = buildVersion();
        LocalDateTime afterCreateTime = LocalDateTime.now();

        versionRepository.save(version);

        Version versionDB = entityManager.find(Version.class, new VersionID("VersionID1"));
        assertEquals(version.getId(), versionDB.getId());
        assertEquals(version.getContent(), versionDB.getContent());
        assertEquals(version.getCreator(), versionDB.getCreator());
        assertTrue(beforeCreateTime.isBefore(versionDB.getCreateTime()) && afterCreateTime.isAfter(versionDB.getCreateTime()));
        assertEquals(version.getRemark(), versionDB.getRemark());
        assertEquals(version.getDraftID(), versionDB.getDraftID());
        assertEquals(version.getLocked(), versionDB.getLocked());
    }

}
