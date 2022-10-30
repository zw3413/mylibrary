package cn.cloudbed.mylibrary.framework.domain.media.response;

import cn.cloudbed.mylibrary.framework.domain.media.MediaFile;
import cn.cloudbed.mylibrary.framework.domain.media.MediaVideoCourse;
import cn.cloudbed.mylibrary.framework.model.response.ResponseResult;
import cn.cloudbed.mylibrary.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by admin on 2018/3/5.
 */
@Data
@ToString
@NoArgsConstructor
public class MediaCourseResult extends ResponseResult {
    public MediaCourseResult(ResultCode resultCode, MediaVideoCourse mediaVideoCourse) {
        super(resultCode);
        this.mediaVideoCourse = mediaVideoCourse;
    }

    MediaFile mediaVideo;
    MediaVideoCourse mediaVideoCourse;
}
