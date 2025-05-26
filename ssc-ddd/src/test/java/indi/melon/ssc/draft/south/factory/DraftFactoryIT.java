package indi.melon.ssc.draft.south.factory;

import indi.melon.ssc.SscBaseTest;
import indi.melon.ssc.directory.common.BizTagProperties;
import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.south.factory.DraftFactory;
import indi.melon.ssc.draft.domain.template.*;
import indi.melon.ssc.ticket.north.local.appservice.TicketAppService;
import indi.melon.ssc.ticket.north.local.message.TicketCreateCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author vvnn1
 * @since 2024/10/29 21:58
 */
@Import(DraftFactoryIT.MockConfiguration.class)
public class DraftFactoryIT extends SscBaseTest {
    @Autowired
    private DraftFactory draftFactory;

    @Test
    public void should_build_draft_from_template_normally(){
        Template template = buildTemplate();
        Draft draft = draftFactory.create(template, "test", "aCreator");
        assertEquals(new DraftID(MockConfiguration.ID_PREFIX + BizTagProperties.DRAFT_BIZ_TAG), draft.getId());
        assertEquals("test", draft.getName());
        assertEquals(template.getContent(), draft.getContent());
        assertEquals(template.getCatalog().name(), draft.getCatalog().name());
        assertEquals(template.getType().name(), draft.getType().name());
        assertEquals("aCreator", draft.getCreator());
        assertEquals("aCreator", draft.getModifier());
    }


    private Template buildTemplate(){
        return new Template(
                new TemplateID("testTemplateId"),
                "templateName",
                "aDesc",
                "aContent",
                TemplateCatalog.STREAM,
                TemplateType.SQL,
                TemplateTag.BASE
        );
    }

    static class MockConfiguration {
        public static String ID_PREFIX="testDraftId-";
        @Bean
        public TicketAppService ticketAppService(){
            return new TicketAppService(null) {
                @Override
                public <T> T require(TicketCreateCommand<T> command) {
                    return (T) (ID_PREFIX + command.bizTag());
                }
            };
        }
    }
}
