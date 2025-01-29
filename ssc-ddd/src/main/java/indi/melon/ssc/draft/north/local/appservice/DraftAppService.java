package indi.melon.ssc.draft.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.domain.common.cqrs.DomainException;
import indi.melon.ssc.draft.domain.draft.*;
import indi.melon.ssc.draft.domain.exception.DraftException;
import indi.melon.ssc.draft.domain.south.client.DraftFileTreeClient;
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
    private final TemplateRepository templateRepository;
    private final DraftRepository draftRepository;
    private final DraftFileTreeClient draftFileTreeClient;
    private final VersionRepository versionRepository;

    public DraftAppService(DraftManager draftManager,
                           TemplateRepository templateRepository,
                           DraftRepository draftRepository,
                           DraftFileTreeClient draftFileTreeClient,
                           VersionRepository versionRepository) {
        this.draftManager = draftManager;
        this.templateRepository = templateRepository;
        this.draftRepository = draftRepository;
        this.draftFileTreeClient = draftFileTreeClient;
        this.versionRepository = versionRepository;
    }

    /**
     * 从模板创建草稿
     * @param command 创建命令
     */
    public Draft create(CreateDraftCommand command) {
        Template template = templateRepository.templateOf(
                new TemplateID(command.templateId())
        );
        if (template == null) {
            throw new ApplicationValidationException("template not found by id: " + command.templateId());
        }

        try {
            Draft draft = draftRepository.save(
                    draftManager.create(
                            template,
                            new EngineID(command.engineId()),
                            command.name(),
                            command.creator()
                    )
            );

            draftFileTreeClient.create(
                    command.directoryId(),
                    draft
            );

            return draft;
        } catch (DomainException e){
            throw new ApplicationDomainException("create draft failed. command: " + command, e);
        }
    }

    /**
     * 另存为模板
     * @param command 另存为命令
     */
    public Draft saveAs(SaveDraftAsCommand command) {
        Draft fromDraft = draftRepository.draftOf(
                new DraftID(command.fromDraftId())
        );

        if (fromDraft == null) {
            throw new ApplicationValidationException("draft not found by id: " + command.fromDraftId());
        }

        try {
            Draft draft = draftRepository.save(
                    draftManager.create(
                            fromDraft,
                            command.name(),
                            command.creator()
                    )
            );

            draftFileTreeClient.create(
                    command.directoryId(),
                    draft
            );

            return draft;
        } catch (DomainException e){
            throw new ApplicationDomainException("copy draft failed. command: " + command, e);
        }
    }

    /**
     * 保存草稿
     * @param command 保存命令
     */
    public void save(SaveDraftCommand command) {
        Draft draft = draftRepository.draftOf(
                new DraftID(command.draftId())
        );
        if (draft == null) {
            throw new ApplicationValidationException("draft not found by id: " + command.draftId());
        }

        try {
            draft.editContent(command.content(), command.modifier());
        } catch (DomainException e) {
            throw new ApplicationDomainException("save draft failed. command: " + command, e);
        }
        draftRepository.save(draft);
    }

    /**
     * 草稿回滚指定版本
     * @param command 回滚命令
     */
    public void rollback(RollbackDraftCommand command) {

        Draft draft = draftRepository.draftOf(
                new DraftID(command.draftId())
        );
        if (draft == null) {
            throw new ApplicationValidationException("draft not found by id: " + command.draftId());
        }

        Version version = versionRepository.versionOf(
                new VersionID(command.versionId())
        );
        if (version == null) {
            throw new ApplicationValidationException("version not found by id: " + command.versionId());
        }

        try {
            draft.rollback(version, command.modifier());
        } catch (DomainException e){
            throw new ApplicationDomainException("rollback draft failed. command: " + command, e);
        }
        draftRepository.save(draft);
    }

    /**
     * 重命名草稿
     * @param command 重命名命令
     */
    public void rename(RenameDraftCommand command) {
        Draft draft = draftRepository.draftOf(
                new DraftID(command.draftId())
        );
        if (draft == null) {
            throw new DraftException("draft not found by id: " + command.draftId());
        }

        try {
            draft.rename(
                    command.newName(),
                    command.modifier()
            );
        } catch (DomainException e){
            throw new ApplicationDomainException("rename draft failed. command: " + command, e);
        }

        draftRepository.save(draft);
    }
}
