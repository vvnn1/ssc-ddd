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
import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.draft.domain.template.Template;
import indi.melon.ssc.draft.domain.template.TemplateID;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import indi.melon.ssc.draft.north.local.message.*;
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
    private final VersionRepository versionRepository;

    public DraftAppService(DraftManager draftManager,
                           DraftFactory draftFactory,
                           TemplateRepository templateRepository,
                           DraftRepository draftRepository,
                           ConfigurationFactory configurationFactory,
                           ConfigurationRepository configurationRepository, VersionRepository versionRepository) {
        this.draftManager = draftManager;
        this.draftFactory = draftFactory;
        this.templateRepository = templateRepository;
        this.draftRepository = draftRepository;
        this.configurationFactory = configurationFactory;
        this.configurationRepository = configurationRepository;
        this.versionRepository = versionRepository;
    }

    /**
     * 从模板创建草稿
     * @param command 创建命令
     */
    public String create(CreateDraftCommand command) {
        Template template = templateRepository.templateOf(
                new TemplateID(command.templateId())
        );

        if (template == null) {
            throw new ApplicationValidationException("not found template by draftId: " + command.templateId());
        }

        Draft draft = draftFactory.create(
                command.name(),
                template,
                command.creator()
        );

        Configuration configuration = new Configuration(draft.getId());
        configuration.assignEngine(
                new EngineID(command.engineId())
        );

        try {
            draftManager.create(
                    draft,
                    configuration,
                    new Directory(
                        command.directory().parentId(),
                        command.directory().rootId()
                    ));
        } catch (DomainException e){
            throw new ApplicationDomainException("create draft failed. command: " + command, e);
        }

        return draft.getId().getValue();
    }

    /**
     * 另存为模板
     * @param command 另存为命令
     */
    public String saveAs(SaveDraftAsCommand command) {
        String fromDraftId = command.fromDraftId();

        Draft draft = nonNullDraft(fromDraftId);
        Draft copyDraft = draftFactory.create(
                command.name(),
                draft,
                command.creator()
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
                            command.directory().parentId(),
                            command.directory().rootId()
                    )
            );
        } catch (DomainException e){
            throw new ApplicationDomainException("copy draft failed. command: " + command, e);
        }

        return copyDraft.getId().getValue();
    }

    /**
     * 保存草稿
     * @param command 保存命令
     */
    public void save(SaveDraftCommand command) {
        Draft draft = nonNullDraft(command.draftId());

        draft.editContent(
                command.content(),
                command.modifier()
        );

        draftRepository.save(draft);
    }

    /**
     * 草稿回滚指定版本
     * @param command 回滚命令
     */
    public void rollback(RollbackDraftCommand command) {
        Draft draft = nonNullDraft(command.draftId());

        Version version = versionRepository.versionOf(
                new VersionID(command.versionId())
        );
        if (version == null){
            throw new ApplicationValidationException("not found version: " + command.versionId());
        }

        try {
            draft.rollback(version, command.modifier());
        } catch (DomainException e){
            throw new ApplicationDomainException("rollback draft failed. command: " + command, e);
        }

        draftRepository.save(draft);
    }


    /**
     * 非空草稿获取，为空则抛异常
     * @param draftId 草稿id
     * @return 草稿
     */
    private Draft nonNullDraft(String draftId) {
        Draft draft = draftRepository.draftOf(
                new DraftID(draftId)
        );

        if (draft == null) {
            throw new ApplicationValidationException("not found draft: " + draftId);
        }

        return draft;
    }
}
