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
@TableName("t_sys_menu")
@ApiModel(value="TSysMenuDto对象", description="")
public class TSysMenuDto extends Model<TSysMenuDto> {

    private static final long serialVersionUID = 1L;

    private String id;

    private String fname;

    private String fnumber;

    private String parentId;

    private String furl;

    private String hasLeaf;

    private String openType;

    private String iconCls;

    private String moduleId;

    private String isdelete;

    private String flevel;

    private String orderno;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
