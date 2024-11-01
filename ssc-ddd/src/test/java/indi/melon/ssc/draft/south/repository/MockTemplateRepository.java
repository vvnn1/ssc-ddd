package indi.melon.ssc.draft.south.repository;

import indi.melon.ssc.draft.domain.south.repository.TemplateRepository;
import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.draft.domain.template.TemplateID;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vvnn1
 * @since 2024/10/30 23:51
 */
public class MockTemplateRepository implements TemplateRepository {
    private final Map<TemplateID, Template> db = new HashMap<>();

    @Override
    public Template templateOf(TemplateID id) {
        return db.get(id);
    }

    @Override
    public void save(Template template) {
        db.put(template.getId(), template);
    }

    @Override
    public void delete(TemplateID id) {
        db.remove(id);
    }
}
