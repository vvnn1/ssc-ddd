package indi.melon.ssc.common.event;

import lombok.Data;

/**
 * @author wangmenglong
 * @since 2025/5/26 17:39
 * 集成事件
 */
@Data
public abstract class IntegrationEvent {
    private String topic;
    private String tag;
    private String key;


}
