package cn.cloudbed.mylibrary.framework.domain.order.response;

import cn.cloudbed.mylibrary.framework.domain.order.XcOrders;

import cn.cloudbed.mylibrary.framework.model.response.ResponseResult;
import cn.cloudbed.mylibrary.framework.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

/**
 * Created by mrt on 2018/3/26.
 */
@Data
@ToString
public class OrderResult extends ResponseResult {
    private XcOrders xcOrders;
    public OrderResult(ResultCode resultCode, XcOrders xcOrders) {
        super(resultCode);
        this.xcOrders = xcOrders;
    }


}
