package indi.melon.ssc.draft.south.repository.dao;

import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangmenglong
 * @since 2024/10/23 19:21
 */
public interface VersionDao extends JpaRepository<Version, VersionID> {
}
