package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.common.exception.ApplicationInfrastructureException;
import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.repository.DraftRepository;
import indi.melon.ssc.draft.south.repository.dao.DraftDao;
import org.springframework.stereotype.Repository;

/**
 * @author wangmenglong
 * @since 2024/10/23 20:01
 */
@Repository
public class DraftRepositoryImpl implements DraftRepository {
    private final DraftDao draftDao;

    public DraftRepositoryImpl(DraftDao draftDao) {
        this.draftDao = draftDao;
    }

    @Override
    public Draft draftOf(DraftID id) {
        try {
            return draftDao.findById(id).orElse(null);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("find draft by " + id + " failed.", e);
        }
    }

    @Override
    public Draft save(Draft draft) {
        try {
            return draftDao.save(draft);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("save draft failed. draft: " + draft, e);
        }
    }

    @Override
    public void delete(DraftID id) {
        try {
            draftDao.deleteById(id);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("delete draft by " + id + " failed.", e);
        }
    }
}
