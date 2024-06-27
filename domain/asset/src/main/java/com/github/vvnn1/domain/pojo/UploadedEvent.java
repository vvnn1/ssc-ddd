package com.github.vvnn1.domain.pojo;

import com.github.vvnn1.cqrs.DomainEvent;
import com.github.vvnn1.domain.entity.AssetID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author vvnn1
 * @since 2024/4/7 12:47
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class UploadedEvent implements DomainEvent {
    private AssetID id;
    private String fileName;
    private String path;
    private String identifier;
    private LocalDateTime createTime;
}
