package indi.melon.ssc.resource.domain.south.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangmenglong
 * @since 2025/4/3 14:41
 */
public class MockRepository <TID, T>{
    protected Map<TID, T> db;

    public MockRepository() {
        this.db = new HashMap<>();
    }
}
