package com.yihexinda.core.abst;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author Jack
 * @date 2018/10/23.
 */
@Data
@JsonIgnoreProperties(value = {"delFlag"})
public abstract class AbstractDto implements Serializable {
    @ApiParam(hidden = true)
    private String createdBy;

    private Date createdAt;

    @ApiParam(hidden = true)
    private String updatedBy;

    private Date updatedAt;

    @ApiParam(hidden = true)
    private Integer delFlag;
}
