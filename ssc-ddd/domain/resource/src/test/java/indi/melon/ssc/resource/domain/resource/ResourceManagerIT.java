package indi.melon.ssc.resource.domain.resource;

import indi.melon.ssc.resource.domain.exception.FileNotFoundException;
import indi.melon.ssc.resource.domain.exception.FileNotMatchException;
import indi.melon.ssc.resource.domain.exception.IllegalRefCountException;
import indi.melon.ssc.resource.domain.exception.ResourceNotFoundException;
import indi.melon.ssc.resource.domain.file.File;
import indi.melon.ssc.resource.domain.file.FileID;
import indi.melon.ssc.resource.domain.south.repository.FileRepository;
import indi.melon.ssc.resource.domain.south.repository.MockFileRepository;
import indi.melon.ssc.resource.domain.south.repository.MockResourceRepository;
import indi.melon.ssc.resource.domain.south.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author wangmenglong
 * @since 2025/4/3 14:23
 */
public class ResourceManagerIT {
    @Test
    public void should_incr_right_ref_count() {
        Resource resource1 = new Resource(
                new ResourceID(1L),
                "resource1",
                new FileID("1")
        );

        Resource resource2 = new Resource(
                new ResourceID(2L),
                "resource2",
                new FileID("1")
        );

        File file1 = new File(
                new FileID("1"),
                "test_url"
        );

        File file2 = new File(
                new FileID("2"),
                "test_url"
        );

        ResourceManager resourceManager = new ResourceManager();

        assertThrows(FileNotMatchException.class, () -> {
            resourceManager.changeRefCount(resource1, file2, 1);
        });

        resourceManager.changeRefCount(resource1, file1, 5);
        assertEquals(5, resource1.getRefCount());
        assertEquals(5,file1.getRefCount());

        resourceManager.changeRefCount(resource2, file1, 3);
        assertEquals(3, resource2.getRefCount());
        assertEquals(8, file1.getRefCount());

        resourceManager.changeRefCount(resource1, file1, -2);
        assertEquals(3, resource1.getRefCount());
        assertEquals(6, file1.getRefCount());

        assertThrows(IllegalRefCountException.class, () -> {
            resourceManager.changeRefCount(resource1, file1, -4);
        });
    }


}
