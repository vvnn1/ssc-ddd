package indi.melon.ssc.resource.domain.resource;

import indi.melon.ssc.resource.domain.file.File;

/**
 * @author wangmenglong
 * @since 2025/4/1 22:15
 */
public class ResourceManager {

    /**
     * 变更资源的引用数量
     * @param resource  资源
     * @param file 文件
     * @param count 变更数量，正数增加，负数减少
     */
    public void changeRefCount(Resource resource, File file, int count) {
        boolean incr = count > 0;
        for (int i = 0; i < Math.abs(count); i++) {
            if (incr) {
                resource.incrRefCount(file);
            } else {
                resource.decrRefCount(file);
            }
        }
    }


}
