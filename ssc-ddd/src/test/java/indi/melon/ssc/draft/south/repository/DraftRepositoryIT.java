package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.repository.DraftRepository;
import indi.melon.ssc.draft.south.repository.dao.DraftDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static indi.melon.ssc.draft.domain.draft.DraftBuildUtil.buildDraft;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author wangmenglong
 * @since 2024/10/23 20:05
 */
@DataJpaTest(
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {DraftRepositoryImpl.class, DraftDao.class}
                )
        }
)
public class DraftRepositoryIT {
    @Autowired
    private DraftRepository draftRepository;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void should_save_and_get_same_draft(){
        Draft draft = buildDraft();
        draftRepository.save(draft);

        Draft draftDB = draftRepository.draftOf(new DraftID("DraftID1"));
        assertEquals(draft.getId(), draftDB.getId());
        assertEquals(draft.getType(), draftDB.getType());
        assertEquals(draft.getContent(), draftDB.getContent());
        assertEquals(draft.getCreator(), draftDB.getCreator());
        assertEquals(draft.getModifier(), draftDB.getModifier());
        assertEquals(draft.getCreateTime(), draftDB.getCreateTime());
        assertEquals(draft.getUpdateTime(), draftDB.getUpdateTime());
    }
}
