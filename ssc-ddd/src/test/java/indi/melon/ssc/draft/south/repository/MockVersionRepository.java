package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangmenglong
 * @since 2024/11/5 18:40
 */
public class MockVersionRepository implements VersionRepository {
    private final Map<VersionID, Version> db = new HashMap<>();

    @Override
    public Version versionOf(VersionID versionID) {
        return db.get(versionID);
    }

    @Override
    public void delete(VersionID versionID) {
        db.remove(versionID);
    }

    @Override
    public void save(Version version) {
        db.put(version.getId(), version);
    }
}
