package indi.melon.ssc.resource.south.repository;

import indi.melon.ssc.resource.domain.resource.Resource;
import indi.melon.ssc.resource.domain.resource.ResourceID;
import indi.melon.ssc.resource.domain.south.repository.ResourceRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangmenglong
 * @since 2025/4/21 18:46
 */
public class MockResourceRepository implements ResourceRepository {
    private final Map<ResourceID, Resource> db = new HashMap<>();
    @Override
    public Resource resourceOf(ResourceID id) {
        return db.get(id);
    }

    @Override
    public Resource save(Resource resource) {
        return db.put(resource.getId(), resource);
    }

    @Override
    public void delete(ResourceID id) {
        db.remove(id);
    }
}
