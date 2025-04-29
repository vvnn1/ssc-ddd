package indi.melon.ssc.resource.south.repository;


import indi.melon.ssc.resource.domain.file.FileID;
import indi.melon.ssc.resource.domain.resource.Resource;
import indi.melon.ssc.resource.domain.resource.ResourceID;
import indi.melon.ssc.resource.domain.south.repository.ResourceRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author wangmenglong
 * @since 2025/4/21 21:09
 */
@DataJpaTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "indi.melon.ssc.resource.south.repository.*"
        )
)
public class ResourceRepositoryIT {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void should_crud_normally() {
        assertNull(entityManager.find(Resource.class, new ResourceID(1L)));

        resourceRepository.save(
                new Resource(
                        new ResourceID(1L),
                        "resource1",
                        new FileID("1")
                )
        );

        assertNotNull(entityManager.find(Resource.class, new ResourceID(1L)));
        assertNotNull( resourceRepository.resourceOf(new ResourceID(1L)));

        resourceRepository.delete(new ResourceID(1L));
        assertNull(entityManager.find(Resource.class, new ResourceID(1L)));
        assertNull( resourceRepository.resourceOf(new ResourceID(1L)));
    }
}
