package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_action")
@ApiModel(value="TSysActionDto对象", description="")
public class TSysActionDto extends Model<TSysActionDto> {

    private static final long serialVersionUID = 1L;

    private String fid;

    private String fname;

    private String fnumber;

    private String description;

    private String moduleId;

    private String orderno;

    private String isdelete;

    private String timeCreate;

    private String creator;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
