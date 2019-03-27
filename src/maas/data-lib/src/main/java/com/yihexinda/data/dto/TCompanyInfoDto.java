package com.yihexinda.data.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 公司内部员工信息表
 * </p>
 *
 * @author 耿东雪
 * @since 2019-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_company_info")
@ApiModel(value="TCompanyInfoDto对象", description="公司内部员工信息表")
public class TCompanyInfoDto extends Model<TCarDto> implements Serializable {

      private static final long serialVersionUID = 1L;

      @ApiModelProperty(value = "主键")
      @TableId(type = IdType.UUID)
      private String id;

      @ApiModelProperty(value = "员工姓名")
      private String userName;

      @ApiModelProperty(value = "部门名称")
      private String deptName;

      @ApiModelProperty(value = "电话号")
      private String phone;

}
