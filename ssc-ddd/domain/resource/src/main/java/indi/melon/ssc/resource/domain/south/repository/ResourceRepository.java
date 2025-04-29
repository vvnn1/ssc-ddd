package indi.melon.ssc.resource.domain.south.repository;

import indi.melon.ssc.resource.domain.resource.Resource;
import indi.melon.ssc.resource.domain.resource.ResourceID;

/**
 * @author wangmenglong
 * @since 2025/4/1 21:54
 */
public interface ResourceRepository {
    Resource resourceOf(ResourceID id);
    Resource save(Resource resource);
    void delete(ResourceID id);
}
