package com.yihexinda.core.abst;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@Data
public abstract class AbstractEntity implements Serializable {
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private Integer delFlag = 0;
}
