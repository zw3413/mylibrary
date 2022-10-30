package cn.cloudbed.mylibrary.rest.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FileDao {

    List getFileList(@Param("param") Map param);

    Integer getFileTotal(@Param("param") Map params);

    Map getFile(@Param("id") String id);

}
