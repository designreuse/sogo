package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 路网由一条条的link组成，link有起点和终点坐标，
link表，中心提供
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_link")
@ApiModel(value="TLinkDto对象", description="路网由一条条的link组成，link有起点和终点坐标，link表，中心提供")
public class TLinkDto extends Model<TLinkDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String linkId;

    @ApiModelProperty(value = "长度")
    private String linkLength;

    @ApiModelProperty(value = "方向（ 0，  1 ）")
    private String linkDir;

    @ApiModelProperty(value = "link起点")
    private String fromNode;

    @ApiModelProperty(value = "link终点")
    private String toNode;


    @Override
    protected Serializable pkVal() {
        return this.linkId;
    }

}
