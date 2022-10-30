package cn.cloudbed.mylibrary.rest.service;

import cn.cloudbed.mylibrary.rest.dao.DbDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DbService {
    private Logger logger= LoggerFactory.getLogger(this.getClass().getCanonicalName());
    @Autowired
    private DbDao dbDao;

    public Object table(String tableName) {
        List<Map> list = dbDao.getTable(tableName);
        return list;
    }
}
