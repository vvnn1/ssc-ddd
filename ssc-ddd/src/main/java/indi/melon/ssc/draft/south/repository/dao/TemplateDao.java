package indi.melon.ssc.draft.south.repository.dao;

import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.draft.domain.template.TemplateID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangmenglong
 * @since 2024/10/30 20:14
 */
public interface TemplateDao extends JpaRepository<Template, TemplateID> {
}
