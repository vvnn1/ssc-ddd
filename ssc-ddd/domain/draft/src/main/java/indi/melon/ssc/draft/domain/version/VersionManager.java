package indi.melon.ssc.draft.domain.version;

import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.draft.domain.version.exception.VersionLockedException;

/**
 * @author vvnn1
 * @since 2024/10/2 20:56
 */
public class VersionManager {
    private final VersionRepository versionRepository;

    public VersionManager(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    public void delete(VersionID versionID) {
        Version version = versionRepository.versionOf(versionID);
        if (version == null){
            return;
        }

        if (Boolean.TRUE.equals(version.isLocked())) {
            throw new VersionLockedException("version " + versionID.value + " is locked, you can not delete it.");
        }

        versionRepository.delete(versionID);
    }
}
