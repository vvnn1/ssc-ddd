package indi.melon.ssc.resource.notrh.local.appservice;

import indi.melon.ssc.SscBaseTest;
import indi.melon.ssc.resource.domain.file.File;
import indi.melon.ssc.resource.domain.file.FileID;
import indi.melon.ssc.resource.domain.resource.Resource;
import indi.melon.ssc.resource.domain.resource.ResourceID;
import indi.melon.ssc.resource.notrh.local.message.ChangeRefCountCommand;
import indi.melon.ssc.resource.notrh.local.message.RenameResourceCommand;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author wangmenglong
 * @since 2025/4/29 19:21
 */
public class ResourceAppServiceIT extends SscBaseTest {
    @Autowired
    private ResourceAppService resourceAppService;
    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void should_rename_normally() {
        entityManager.persist(
                new Resource(
                        new ResourceID(1L),
                        "oldName",
                        new FileID("1")
                )
        );

        LocalDateTime beforeRenameTime = LocalDateTime.now();
        resourceAppService.rename(
                 new RenameResourceCommand(
                         1L,
                         "newName"
                 )
         );
        LocalDateTime afterRenameTime = LocalDateTime.now();

        Resource resource = entityManager.find(Resource.class, new ResourceID(1L));
        assertEquals("newName", resource.getName());
        assertTrue(beforeRenameTime.isBefore(resource.getUpdateTime()) && afterRenameTime.isAfter(resource.getUpdateTime()));

        entityManager.remove(
                resource
        );
    }

    @Test
    @Transactional
    public void should_change_ref_count_normally() {
        entityManager.persist(
                new Resource(
                        new ResourceID(1L),
                        "oldName",
                        new FileID("1")
                )
        );
        entityManager.persist(
                new File(
                        new FileID("1"),
                        "test_url"
                )
        );

        resourceAppService.changeRefCount(
                new ChangeRefCountCommand(
                        1L,
                        1
                )
        );

        Resource resource = entityManager.find(Resource.class, new ResourceID(1L));
        File file = entityManager.find(File.class, new FileID("1"));
        assertEquals(1, resource.getRefCount());
        assertEquals(1, file.getRefCount());
    }
}
