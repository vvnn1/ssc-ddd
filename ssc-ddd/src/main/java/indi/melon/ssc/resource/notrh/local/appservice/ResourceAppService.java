package indi.melon.ssc.resource.notrh.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.domain.common.cqrs.DomainException;
import indi.melon.ssc.resource.domain.file.File;
import indi.melon.ssc.resource.domain.resource.Resource;
import indi.melon.ssc.resource.domain.resource.ResourceID;
import indi.melon.ssc.resource.domain.resource.ResourceManager;
import indi.melon.ssc.resource.domain.south.repository.FileRepository;
import indi.melon.ssc.resource.domain.south.repository.ResourceRepository;
import indi.melon.ssc.resource.notrh.local.message.ChangeRefCountCommand;
import indi.melon.ssc.resource.notrh.local.message.RenameResourceCommand;
import org.springframework.stereotype.Service;

/**
 * @author wangmenglong
 * @since 2025/4/19 16:12
 */
@Service
public class ResourceAppService {
    private final ResourceRepository resourceRepository;
    private final FileRepository fileRepository;
    private final ResourceManager resourceManager;

    public ResourceAppService(ResourceManager resourceManager,
                              ResourceRepository resourceRepository,
                              FileRepository fileRepository) {
        this.resourceRepository = resourceRepository;
        this.resourceManager = resourceManager;
        this.fileRepository = fileRepository;
    }

    /**
     * 重命名资源
     *
     * @param command 命令
     */
    public void rename(RenameResourceCommand command) {
        Resource resource = resourceRepository.resourceOf(
                new ResourceID(command.id())
        );

        if (resource == null) {
            throw new ApplicationValidationException("resource is not exist.id: " + command.id());
        }

        try {
            resource.rename(
                    command.newName()
            );
        } catch (DomainException e) {
            throw new ApplicationDomainException("rename resource failed.", e);
        }
    }

    /**
     * 变更资源引用
     *
     * @param command 命令
     */
    public void changeRefCount(ChangeRefCountCommand command) {
        Resource resource = resourceRepository.resourceOf(
                new ResourceID(command.id())
        );

        if (resource == null) {
            throw new ApplicationValidationException("resource is not exist.id: " + command.id());
        }

        File file = fileRepository.fileOf(resource.getFileID());
        if (file == null) {
            throw new ApplicationValidationException("file is not exist.id: " + resource.getFileID());
        }

        try {
            resourceManager.changeRefCount(resource, file, command.refCount());
        } catch (DomainException e) {
            throw new ApplicationDomainException("change ref count failed.", e);
        }
    }
}
