package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.repository.DraftRepository;
import indi.melon.ssc.draft.south.repository.dao.DraftDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static indi.melon.ssc.draft.domain.draft.DraftBuildUtil.buildDraft;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void should_save_and_get_same_draft(){
        LocalDateTime beforeCreateTime = LocalDateTime.now();
        Draft draft = buildDraft();
        LocalDateTime afterCreateTime = LocalDateTime.now();

        draftRepository.save(draft);

        Draft draftDB = entityManager.find(Draft.class, new DraftID("DraftID1"));
        assertEquals(draft.getId(), draftDB.getId());
        assertEquals(draft.getCatalog(), draftDB.getCatalog());
        assertEquals(draft.getName(), draftDB.getName());
        assertEquals(draft.getContent(), draftDB.getContent());
        assertEquals(draft.getCreator(), draftDB.getCreator());
        assertEquals(draft.getModifier(), draftDB.getModifier());
        assertTrue(beforeCreateTime.isBefore(draft.getCreateTime()) && afterCreateTime.isAfter(draft.getCreateTime()));
    }
}
