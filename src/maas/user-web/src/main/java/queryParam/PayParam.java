package queryParam;

import com.yihexinda.core.param.BaseParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/7 0007
 */
@Data
@ApiModel("支付接口参数")
public class PayParam extends BaseParams {

    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")//
    private String orderNo;

    /**
     * 过期时间
     */
    @ApiModelProperty("openid")//
    private String openid;


}
