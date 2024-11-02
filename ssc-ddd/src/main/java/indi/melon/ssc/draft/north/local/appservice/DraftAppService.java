package indi.melon.ssc.draft.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.domain.common.cqrs.DomainException;
import indi.melon.ssc.draft.domain.configuration.Configuration;
import indi.melon.ssc.draft.domain.configuration.ConfigurationID;
import indi.melon.ssc.draft.domain.configuration.EngineID;
import indi.melon.ssc.draft.domain.draft.Directory;
import indi.melon.ssc.draft.domain.draft.Draft;
import indi.melon.ssc.draft.domain.draft.DraftID;
import indi.melon.ssc.draft.domain.draft.DraftManager;
import indi.melon.ssc.draft.domain.south.factory.ConfigurationFactory;
import indi.melon.ssc.draft.domain.south.factory.DraftFactory;
import indi.melon.ssc.draft.domain.south.repository.ConfigurationRepository;
import indi.melon.ssc.draft.domain.south.repository.DraftRepository;
import indi.melon.ssc.draft.domain.south.repository.TemplateRepository;
import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.draft.domain.template.TemplateID;
import indi.melon.ssc.draft.north.local.message.SaveDraftAsCommand;
import indi.melon.ssc.draft.north.local.message.CreateDraftCommand;
import indi.melon.ssc.draft.north.local.message.SaveDraftCommand;
import org.springframework.stereotype.Service;

/**
 * @author vvnn1
 * @since 2024/10/27 21:19
 */
@Service
public class DraftAppService {
    private final DraftManager draftManager;
    private final DraftFactory draftFactory;
    private final TemplateRepository templateRepository;
    private final DraftRepository draftRepository;
    private final ConfigurationFactory configurationFactory;
    private final ConfigurationRepository configurationRepository;

    public DraftAppService(DraftManager draftManager,
                           DraftFactory draftFactory,
                           TemplateRepository templateRepository,
                           DraftRepository draftRepository,
                           ConfigurationFactory configurationFactory,
                           ConfigurationRepository configurationRepository) {
        this.draftManager = draftManager;
        this.draftFactory = draftFactory;
        this.templateRepository = templateRepository;
        this.draftRepository = draftRepository;
        this.configurationFactory = configurationFactory;
        this.configurationRepository = configurationRepository;
    }

    /**
     * 从模板创建草稿
     * @param createCommand 创建命令
     */
    public String create(CreateDraftCommand createCommand) {
        Template template = templateRepository.templateOf(
                new TemplateID(createCommand.templateId())
        );

        if (template == null) {
            throw new ApplicationValidationException("not found template by id: " + createCommand.templateId());
        }

        Draft draft = draftFactory.create(
                createCommand.name(),
                template,
                createCommand.creator()
        );

        Configuration configuration = new Configuration(draft.getId());
        configuration.assignEngine(
                new EngineID(createCommand.engineId())
        );

        try {
            draftManager.create(
                    draft,
                    configuration,
                    new Directory(
                        createCommand.directory().parentId(),
                        createCommand.directory().rootId()
                    ));
        } catch (DomainException e){
            throw new ApplicationDomainException("create draft failed. command: " + createCommand, e);
        }

        return draft.getId().getValue();
    }

    /**
     * 另存为模板
     * @param saveDraftAsCommand 另存为命令
     */
    public String saveAs(SaveDraftAsCommand saveDraftAsCommand) {
        String fromDraftId = saveDraftAsCommand.fromDraftId();

        DraftID draftID = new DraftID(fromDraftId);
        Draft draft = draftRepository.draftOf(draftID);
        if (draft == null){
            throw new ApplicationValidationException("not found draft: " + fromDraftId);
        }
        Draft copyDraft = draftFactory.create(
                saveDraftAsCommand.name(),
                draft,
                saveDraftAsCommand.creator()
        );

        Configuration configuration = configurationRepository.configurationOf(
                new ConfigurationID(fromDraftId)
        );
        Configuration copyConfiguration = configurationFactory.create(copyDraft.getId(), configuration);

        try {
            draftManager.create(
                    copyDraft,
                    copyConfiguration,
                    new Directory(
                            saveDraftAsCommand.directory().parentId(),
                            saveDraftAsCommand.directory().rootId()
                    )
            );
        } catch (DomainException e){
            throw new ApplicationDomainException("copy draft failed. command: " + saveDraftAsCommand, e);
        }

        return copyDraft.getId().getValue();
    }

    /**
     * 保存草稿
     * @param saveDraftCommand 保存命令
     */
    void save(SaveDraftCommand saveDraftCommand) {
        Draft draft = draftRepository.draftOf(new DraftID(saveDraftCommand.draftId()));
        if (draft == null){
            throw new ApplicationValidationException("not found draft: " + saveDraftCommand.draftId());
        }

        draft.editContent(
                saveDraftCommand.content(),
                saveDraftCommand.modifier()
        );

        draftRepository.save(draft);
    }
}
