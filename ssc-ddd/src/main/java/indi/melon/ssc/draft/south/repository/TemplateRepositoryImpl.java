package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.common.exception.ApplicationInfrastructureException;
import indi.melon.ssc.draft.domain.south.repository.TemplateRepository;
import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.draft.domain.template.TemplateID;
import indi.melon.ssc.draft.south.repository.dao.TemplateDao;
import org.springframework.stereotype.Repository;

/**
 * @author wangmenglong
 * @since 2024/10/30 20:14
 */
@Repository
public class TemplateRepositoryImpl implements TemplateRepository {
    private final TemplateDao templateDao;

    public TemplateRepositoryImpl(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    @Override
    public Template templateOf(TemplateID id) {
        try {
            return templateDao.findById(id).orElse(null);
        } catch (Exception e) {
            throw new ApplicationInfrastructureException("find template by " + id + " failed.", e);
        }
    }

    @Override
    public void save(Template template) {
        try {
            templateDao.save(template);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("save template failed. template: " + template, e);
        }
    }

    @Override
    public void delete(TemplateID id) {
        try {
            templateDao.deleteById(id);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("delete template by " + id + " failed.", e);
        }
    }
}
