package indi.melon.ssc.draft.domain.template;

/**
 * @author vvnn1
 * @since 2024/10/28 23:24
 */
public class TemplateUtil {
    public static Template buildTemplate(String id, String content){
        return new Template(
                new TemplateID(id),
                "testTemplate" + id,
                "a test template",
                content,
                TemplateCatalog.STREAM,
                TemplateType.SQL,
                TemplateTag.BASE
        );
    }
}
