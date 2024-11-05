package indi.melon.ssc.draft.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import indi.melon.ssc.draft.domain.version.VersionManager;
import indi.melon.ssc.draft.north.local.message.DeleteVersionCommand;
import indi.melon.ssc.draft.north.local.message.LockVersionCommand;
import indi.melon.ssc.draft.north.local.message.UnLockVersionCommand;
import indi.melon.ssc.draft.south.repository.MockVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangmenglong
 * @since 2024/11/5 18:38
 */
public class VersionAppServiceIT {
    private VersionRepository versionRepository;
    private VersionAppService versionAppService;
    private VersionManager versionManager;

    @BeforeEach
    public void init() {
        versionRepository = new MockVersionRepository();
        versionManager = new VersionManager(versionRepository);
        versionAppService = new VersionAppService(versionRepository, versionManager);

        versionRepository.save(
                new Version(
                        new VersionID("version1"),
                        new DraftID("draft1"),
                        "versionContent",
                        "noRemark",
                        "vvnn1"
                )
        );
    }

    @Test
    public void should_lock_and_unlock_version_normally() {
        Version version = versionRepository.versionOf(new VersionID("version1"));
        assertNotNull(version);
        assertFalse(version.isLocked());

        versionAppService.lock(
                new LockVersionCommand("version1")
        );

        version = versionRepository.versionOf(new VersionID("version1"));
        assertTrue(version.isLocked());

        versionAppService.unlock(
                new UnLockVersionCommand("version1")
        );

        version = versionRepository.versionOf(new VersionID("version1"));
        assertFalse(version.isLocked());
    }

    @Test
    public void should_delete_version_normally() {
        versionAppService.lock(
                new LockVersionCommand("version1")
        );

        assertThrows(ApplicationDomainException.class, () -> versionAppService.delete(
                new DeleteVersionCommand("version1")
        ));

        versionAppService.unlock(
                new UnLockVersionCommand("version1")
        );

        versionAppService.delete(
                new DeleteVersionCommand("version1")
        );

        Version version = versionRepository.versionOf(new VersionID("version1"));
        assertNull(version);
    }
}
