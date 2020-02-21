package cn.cloudbed.mylibrary.rest.dao;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BookDao {
    List<Map> queryBook(@Param("param") Map<String,Object> param);



    void saveBook(@Param("book") Map<String, Object> book);

    Integer bookTotal(@Param("param") Map<String,Object> param);

    List getClassification();

    List getPublishhouse();

    List getAuthor();

    void createBook(@Param("book") Map<String, Object> book);

    Map<String, Object> getBookById(@Param("id")String id);
}
