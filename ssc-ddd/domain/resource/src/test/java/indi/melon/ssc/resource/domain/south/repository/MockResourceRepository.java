package indi.melon.ssc.resource.domain.south.repository;

import indi.melon.ssc.resource.domain.resource.Resource;
import indi.melon.ssc.resource.domain.resource.ResourceID;

/**
 * @author wangmenglong
 * @since 2025/4/3 14:43
 */
public class MockResourceRepository extends MockRepository<ResourceID, Resource> implements ResourceRepository {
    @Override
    public Resource resourceOf(ResourceID id) {
        return db.get(id);
    }

    @Override
    public Resource save(Resource resource) {
        db.put(resource.getId(), resource);
        return resource;
    }

    @Override
    public void delete(ResourceID id) {
        db.remove(id);
    }
}
