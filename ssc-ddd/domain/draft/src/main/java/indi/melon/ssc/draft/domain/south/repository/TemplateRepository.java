package indi.melon.ssc.draft.domain.south.repository;

import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.draft.domain.template.TemplateID;

/**
 * @author wangmenglong
 * @since 2024/10/30 19:52
 */
public interface TemplateRepository {
    Template templateOf(TemplateID id);
    void save(Template template);
    void delete(TemplateID id);
}
