package cn.cloudbed.mylibrary.framework.domain.cms.response;

import cn.cloudbed.mylibrary.framework.model.response.ResponseResult;
import cn.cloudbed.mylibrary.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @version 1.0
 **/
@Data
@NoArgsConstructor
public class CmsPostPageResult extends ResponseResult {

    String pageUrl;
    public CmsPostPageResult(ResultCode resultCode, String pageUrl) {
        super(resultCode);
        this.pageUrl = pageUrl;
    }
}
