package cn.cloudbed.mylibrary.rest.service;

import cn.cloudbed.common.exception.ServiceException;
import cn.cloudbed.mylibrary.rest.dao.FileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {
    private Logger logger= LoggerFactory.getLogger(this.getClass().getCanonicalName());
    @Autowired
    private FileDao fileDao;



    public Object getFile(String pageSize, String currentPage, String name) {
        Map<String,Object> params=new HashMap<>();
        params.put("pageSize",pageSize);
        params.put("page",currentPage);
        if(name!=null && name.length()>0) params.put("name", name);
        List bookList=(List)getFileList(params);

        Integer total=getFileTotal(params);
        Map<String,Object> map=new HashMap<>();
        map.put("items",bookList);
        map.put("count",total);
        return map;
    }

    private List<Map> getFileList(Map param){
        try {
            if (param == null) param = new HashMap();

            String testKey;
            testKey = "pageSize";
            if (param.get(testKey) == null) param.put(testKey, "15");
            Integer pageSize= Integer.parseInt( (String)param.get(testKey));
            testKey = "page";
            if (param.get(testKey) == null) param.put(testKey, "1");
            Integer page= Integer.parseInt( (String)param.get(testKey));
            param.put("offset", pageSize*(page-1));

            List<Map> result = fileDao.getFileList( param);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
    private Integer getFileTotal(Map params){
        return fileDao.getFileTotal(params);
    }

    public Object getFile(String id) {
        return fileDao.getFile(id);
    }

    public Object updateFile(String id, Map<String, Object> fileMap) {
        return null;
    }

    public Object deleteFile(String id) {
        return null;
    }

}
