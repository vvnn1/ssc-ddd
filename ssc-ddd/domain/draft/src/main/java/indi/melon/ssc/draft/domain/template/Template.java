package indi.melon.ssc.draft.domain.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vvnn1
 * @since 2024/10/27 21:36
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class Template {
    private TemplateID id;
    private String name;
    private String desc;
    private String content;
    private TemplateCatalog catalog;
    private TemplateType type;
    private TemplateTag tag;

    public Template() {
    }

    public Template(TemplateID id,
                    String name,
                    String desc,
                    String content,
                    TemplateCatalog catalog,
                    TemplateType type,
                    TemplateTag tag) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.content = content;
        this.catalog = catalog;
        this.type = type;
        this.tag = tag;
    }
}
