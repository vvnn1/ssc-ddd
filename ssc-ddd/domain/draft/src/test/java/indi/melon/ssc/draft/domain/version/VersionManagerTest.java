package indi.melon.ssc.draft.domain.version;

import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.draft.domain.version.exception.VersionLockedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vvnn1
 * @since 2024/10/2 21:33
 */
public class VersionManagerTest {
    private VersionManager versionManager;
    private VersionRepository versionRepository;

    @BeforeEach
    public void init() {
        versionRepository = new TestVersionRepository();
        versionManager = new VersionManager(versionRepository);
    }


    @Test
    public void should_delete_version_by_id() {
        versionManager.delete(new VersionID("1"));
        assertNull(versionRepository.versionOf(new VersionID("1")));

        assertThrows(VersionLockedException.class, () -> versionManager.delete(new VersionID("2")));
    }

    static class TestVersionRepository implements VersionRepository{
        private final Map<VersionID, Version> dbMap = new HashMap<>();

        {
            Version normalVersion = new Version();
            normalVersion.setId(new VersionID("1"));
            normalVersion.setContent("aa");
            normalVersion.setCreateTime(LocalDateTime.now());
            normalVersion.setCreator("tester");
            normalVersion.setRemark("-");
            normalVersion.setLocked(false);
            normalVersion.setDraftID(new DraftID("d1"));
            dbMap.put(new VersionID("1"), normalVersion);

            Version lockedVersion = new Version();
            lockedVersion.setId(new VersionID("2"));
            lockedVersion.setContent("locked");
            lockedVersion.setCreateTime(LocalDateTime.now());
            lockedVersion.setCreator("tester");
            lockedVersion.setRemark("-");
            lockedVersion.setLocked(true);
            lockedVersion.setDraftID(new DraftID("d2"));
            dbMap.put(new VersionID("2"), lockedVersion);
        }

        @Override
        public Version versionOf(VersionID versionID) {
            return dbMap.get(versionID);
        }

        @Override
        public void delete(VersionID versionID) {
            dbMap.remove(versionID);
        }

        @Override
        public void save(Version version) {
            dbMap.put(version.getId(), version);
        }
    }
}
