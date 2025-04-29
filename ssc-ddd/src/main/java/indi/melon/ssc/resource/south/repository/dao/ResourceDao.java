package indi.melon.ssc.resource.south.repository.dao;

import indi.melon.ssc.resource.domain.resource.Resource;
import indi.melon.ssc.resource.domain.resource.ResourceID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangmenglong
 * @since 2025/4/21 19:06
 */
public interface ResourceDao extends JpaRepository<Resource, ResourceID> {
}
