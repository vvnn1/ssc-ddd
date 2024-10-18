package indi.melon.ssc.draft.domain.south.repository;

import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;

/**
 * @author vvnn1
 * @since 2024/10/2 21:23
 */
public interface VersionRepository {
    Version versionOf(VersionID versionID);
    void delete(Version version);
    void save(Version version);
}
