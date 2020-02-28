package com.seckill.server.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 秒杀记录dto
 */
@Data
@ToString
public class KillDto implements Serializable {

    @NotNull
    private Integer killId;

    private Integer userId;

}