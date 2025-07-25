package indi.melon.ssc.draft.north.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.domain.common.cqrs.DomainException;
import indi.melon.ssc.draft.domain.south.repository.VersionRepository;
import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;
import indi.melon.ssc.draft.domain.version.VersionManager;
import indi.melon.ssc.draft.north.local.message.DeleteVersionCommand;
import indi.melon.ssc.draft.north.local.message.LockVersionCommand;
import indi.melon.ssc.draft.north.local.message.UnLockVersionCommand;

/**
 * @author wangmenglong
 * @since 2024/11/5 18:34 
 */
public class VersionAppService {
    private final VersionRepository versionRepository;
    private final VersionManager versionManager;

    public VersionAppService(VersionRepository versionRepository, VersionManager versionManager) {
        this.versionRepository = versionRepository;
        this.versionManager = versionManager;
    }

    /**
     * 锁定草稿版本
     * @param command 锁定命令
     */
    public void lock(LockVersionCommand command) {
        Version version = versionRepository.versionOf(
                new VersionID(command.versionId())
        );

        if (version == null){
            throw new ApplicationValidationException("not found version by draftId: " + command.versionId());
        }

        version.lock();
    }

    /**
     * 解锁草稿版本
     * @param command 解锁命令
     */
    public void unlock(UnLockVersionCommand command) {
        Version version = versionRepository.versionOf(
                new VersionID(command.versionId())
        );

        if (version == null){
            throw new ApplicationValidationException("not found version by versionId: " + command.versionId());
        }

        version.unlock();
    }

    /**
     * 删除版本命令
     * @param command  删除命令
     */
    public void delete(DeleteVersionCommand command){
        try {
            versionManager.delete(
                    new VersionID(command.id())
            );
        } catch (DomainException e){
            throw new ApplicationDomainException("delete version failed: " + command.id(), e);
        }
    }
}
