package com.yihexinda.data.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author Jack
 * @date 2018/11/15.
 */
@Data
@ToString
public class UserAuthorityDto {
    private Integer userId;
    private String authorityName;
    private Integer enable;
}
