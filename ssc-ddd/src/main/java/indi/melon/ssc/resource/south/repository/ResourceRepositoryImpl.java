package indi.melon.ssc.resource.south.repository;

import indi.melon.ssc.common.exception.ApplicationInfrastructureException;
import indi.melon.ssc.resource.domain.resource.Resource;
import indi.melon.ssc.resource.domain.resource.ResourceID;
import indi.melon.ssc.resource.domain.south.repository.ResourceRepository;
import indi.melon.ssc.resource.south.repository.dao.ResourceDao;
import org.springframework.stereotype.Repository;

/**
 * @author wangmenglong
 * @since 2025/4/21 19:01
 */
@Repository
public class ResourceRepositoryImpl implements ResourceRepository {
    private final ResourceDao resourceDao;

    public ResourceRepositoryImpl(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    @Override
    public Resource resourceOf(ResourceID id) {
        try {
            return resourceDao.findById(id).orElse(null);
        } catch (Exception e) {
            throw new ApplicationInfrastructureException("find resource by " + id + " failed.", e);
        }
    }

    @Override
    public Resource save(Resource resource) {
        try {
            return resourceDao.save(resource);
        } catch (Exception e) {
            throw new ApplicationInfrastructureException("save resource failed. resource: " + resource, e);
        }
    }

    @Override
    public void delete(ResourceID id) {
        try {
            resourceDao.deleteById(id);
        } catch (Exception e){
            throw new ApplicationInfrastructureException("delete resource by " + id + " failed.", e);
        }
    }
}
