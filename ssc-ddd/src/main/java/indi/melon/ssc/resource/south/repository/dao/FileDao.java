package indi.melon.ssc.resource.south.repository.dao;

import indi.melon.ssc.resource.domain.file.File;
import indi.melon.ssc.resource.domain.file.FileID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangmenglong
 * @since 2025/4/21 21:20
 */
public interface FileDao extends JpaRepository<File, FileID> {
}
