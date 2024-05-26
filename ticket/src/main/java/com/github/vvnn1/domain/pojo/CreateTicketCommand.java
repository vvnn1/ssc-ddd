package com.github.vvnn1.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author vvnn1
 * @since 2024/4/9 15:02
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class CreateTicketCommand<T> {
    private String bizTag;
}
