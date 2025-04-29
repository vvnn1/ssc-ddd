package indi.melon.ssc.resource.domain.resource;

import indi.melon.ssc.resource.domain.exception.FileNotMatchException;
import indi.melon.ssc.resource.domain.exception.IllegalRefCountException;
import indi.melon.ssc.resource.domain.file.File;
import indi.melon.ssc.resource.domain.file.FileID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author wangmenglong
 * @since 2025/3/31 15:42
 */
public class ResourceTest {
    @Test
    public void should_incr_ref_count_in_the_same_time() {
        FileID fileID = new FileID("112233");
        Resource resource1 = createResource(fileID);
        Resource resource2 = createResource(fileID);
        File file = createOssFile(fileID);

        assertEquals(0, resource1.getRefCount());
        assertEquals(0, resource2.getRefCount());
        assertEquals(0, file.getRefCount());

        assertThrows(FileNotMatchException.class, () -> resource1.incrRefCount(
                new File(
                        new FileID("2"),
                        "testUrl"
                )
        ));

        assertThrows(FileNotMatchException.class, () -> resource1.decrRefCount(
                new File(
                        new FileID("2"),
                        "testUrl"
                )
        ));


        resource1.incrRefCount(file);
        assertEquals(1, resource1.getRefCount());
        assertEquals(0, resource2.getRefCount());
        assertEquals(1, file.getRefCount());


        resource2.incrRefCount(file);
        assertEquals(1, resource1.getRefCount());
        assertEquals(1, resource2.getRefCount());
        assertEquals(2, file.getRefCount());

        resource1.decrRefCount(file);
        assertEquals(0, resource1.getRefCount());
        assertEquals(1, resource2.getRefCount());
        assertEquals(1, file.getRefCount());

        resource2.decrRefCount(file);
        assertEquals(0, resource1.getRefCount());
        assertEquals(0, resource2.getRefCount());
        assertEquals(0, file.getRefCount());

        assertThrows(IllegalRefCountException.class, () -> resource2.decrRefCount(file));
    }

    private Resource createResource(FileID fileID) {
        return new Resource(
                new ResourceID(1L),
                "testResource",
                fileID
        );
    }


    private File createOssFile(FileID fileID) {
        return new File(
                fileID,
                "testFile"
        );
    }
}
