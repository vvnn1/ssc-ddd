package indi.melon.ssc.resource.notrh.local.appservice;

import indi.melon.ssc.common.exception.ApplicationDomainException;
import indi.melon.ssc.common.exception.ApplicationValidationException;
import indi.melon.ssc.resource.domain.file.File;
import indi.melon.ssc.resource.domain.file.FileID;
import indi.melon.ssc.resource.domain.resource.Resource;
import indi.melon.ssc.resource.domain.resource.ResourceID;
import indi.melon.ssc.resource.domain.resource.ResourceManager;
import indi.melon.ssc.resource.notrh.local.message.ChangeRefCountCommand;
import indi.melon.ssc.resource.notrh.local.message.RenameResourceCommand;
import indi.melon.ssc.resource.south.repository.MockFileRepository;
import indi.melon.ssc.resource.south.repository.MockResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangmenglong
 * @since 2025/4/21 14:05
 */
public class ResourceAppServiceTest {
    private ResourceAppService resourceAppService;
    private MockResourceRepository mockResourceRepository;
    private MockFileRepository mockFileRepository;

    @BeforeEach
    public void setUp() {
        mockResourceRepository = new MockResourceRepository();
        mockFileRepository = new MockFileRepository();
        resourceAppService = new ResourceAppService(
                new ResourceManager(),
                mockResourceRepository,
                mockFileRepository
        );
    }

    @Test
    public void should_rename_resource_normally() {
        Resource resource = new Resource(
                new ResourceID(1L),
                "oldName",
                new FileID("1")
        );

        mockResourceRepository.save(
                resource
        );

        assertThrows(ApplicationValidationException.class, () -> {
            resourceAppService.rename(
                    new RenameResourceCommand(
                            null,
                            "newName"
                    )
            );
        });

        assertThrows(ApplicationValidationException.class, () -> {
            resourceAppService.rename(
                    new RenameResourceCommand(
                            1L,
                            null
                    )
            );
        });

        assertThrows(ApplicationValidationException.class, () -> {
            resourceAppService.rename(
                    new RenameResourceCommand(
                            1L,
                            ""
                    )
            );
        });

        assertThrows(ApplicationValidationException.class, () -> {
           resourceAppService.rename(
                   new RenameResourceCommand(
                           2L,
                           "newName"
                   )
           );
        });

        LocalDateTime beforeRename = LocalDateTime.now();
        assertDoesNotThrow(() -> {
            resourceAppService.rename(
                    new RenameResourceCommand(
                            1L,
                            "newName"
                    )
            );
        });
        LocalDateTime afterRename = LocalDateTime.now();

        assertEquals("newName", resource.getName());
        assertTrue(beforeRename.isBefore(resource.getUpdateTime()) && afterRename.isAfter(resource.getUpdateTime()));
    }

    @Test
    public void should_change_ref_count_normally() {
        Resource resource1 = new Resource(
                new ResourceID(1L),
                "resource1",
                new FileID("1")
        );
        mockResourceRepository.save(resource1);
        File file1 = new File(
                new FileID("1"),
                "test_url"
        );
        mockFileRepository.save(
                file1
        );

        assertThrows(ApplicationValidationException.class, () -> {
            resourceAppService.changeRefCount(
                    new ChangeRefCountCommand(
                            null,
                            1
                    )
            );
        });

        assertThrows(ApplicationValidationException.class, () -> {
            resourceAppService.changeRefCount(
                    new ChangeRefCountCommand(
                            1L,
                            null
                    )
            );
        });

        assertThrows(ApplicationValidationException.class, () -> {
            resourceAppService.changeRefCount(
                    new ChangeRefCountCommand(
                            3L,
                            1
                    )
            );
        });

        Resource resource2 = new Resource(
                new ResourceID(2L),
                "resource2",
                new FileID("2")
        );
        mockResourceRepository.save(resource2);
        assertThrows(ApplicationValidationException.class, () -> {
            resourceAppService.changeRefCount(
                    new ChangeRefCountCommand(
                            2L,
                            1
                    )
            );
        });

        assertEquals(0L, resource1.getRefCount());
        assertThrows(ApplicationDomainException.class, () ->
            resourceAppService.changeRefCount(
                    new ChangeRefCountCommand(
                            1L,
                            -1
                    )
            )
        );
    }
}
