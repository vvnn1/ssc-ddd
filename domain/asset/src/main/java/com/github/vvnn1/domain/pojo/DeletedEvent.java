package com.github.vvnn1.domain.pojo;

import com.github.vvnn1.cqrs.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author vvnn1
 * @since 2024/4/7 12:59
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class DeletedEvent implements DomainEvent {
    private Long id;
}
