package indi.melon.ssc.draft.south.repository.dao;

import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangmenglong
 * @since 2024/10/23 20:02
 */
public interface DraftDao extends JpaRepository<Draft, DraftID> {
}
