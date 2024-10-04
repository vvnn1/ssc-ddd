package indi.melon.ssc.draft.context.south.repository;

import indi.melon.ssc.draft.context.domain.version.Version;
import indi.melon.ssc.draft.context.domain.version.VersionID;

/**
 * @author vvnn1
 * @since 2024/10/2 21:23
 */
public interface VersionRepository {
    Version versionOf(VersionID versionID);
    void remove(Version version);
    void save(Version version);
}
