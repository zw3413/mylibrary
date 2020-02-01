package cn.cloudbed.mylibrary.book;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BookMapper {
    List<Map> queryBook(@Param("param") Map<String,Object> param);

    List<Map> getTable(@Param("tableName")String tableName);

    void saveBook(@Param("book") Map<String, Object> book);

    Integer bookTotal(@Param("param") Map<String,Object> param);
}
