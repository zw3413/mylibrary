package cn.cloudbed.mylibrary.rest.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DbDao {
    List<Map> getTable(@Param("tableName") String tableName);
}
