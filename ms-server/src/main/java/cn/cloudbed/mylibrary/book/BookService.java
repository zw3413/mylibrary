package cn.cloudbed.mylibrary.book;

import cn.cloudbed.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("bookService")
public class BookService {

    @Autowired
    private BookMapper bookMapper;

    public Object book_v2(String pageSize, String currentPage,String bookName){
        Map<String,Object> params=new HashMap<>();
        params.put("pageSize",pageSize);
        params.put("page",currentPage);
        if(bookName!=null && bookName.length()>0) params.put("name", bookName);
        List bookList=(List)book(params);

        Integer total=bookTotal(params);
        Map<String,Object> map=new HashMap<>();
        map.put("items",bookList);
        map.put("count",total);
        return map;
    }

    private Integer bookTotal(Map params) {
        return bookMapper.bookTotal(params);
    }

    public Object book(Map<String, Object> param) {
        try {
            if (param == null) param = new HashMap();
            List<String> emptyKeyList = new ArrayList<String>();
            for (String key : param.keySet()) {
                if (StringUtils.isEmpty(param.get(key))) {
                    emptyKeyList.add(key);
                }
            }
            for (String emptyKey : emptyKeyList) {
                param.remove(emptyKey);
            }

            String key = "classification";
            if (param.get(key) != null) {
                List<String> cArr = (List) param.get(key);
                if (cArr.size() > 1) {
                    param.put("classification", String.join("/", cArr));
                } else {
                    param.put("classification", cArr.get(0));
                }
            }

            key = "pageSize";
            if (param.get(key) == null) param.put(key, "15");
            //param.put(key, Integer.parseInt((String)param.get(key)));
            Integer pageSize= Integer.parseInt( (String)param.get(key));
            key = "page";
            if (param.get(key) == null) param.put(key, "1");
            //param.put(key, Integer.parseInt((String)param.get(key)));
            Integer page= Integer.parseInt( (String)param.get(key));

            param.put("offset", pageSize*(page-1));

            Object books = bookMapper.queryBook(param);
            return books;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    public Object table(String tableName) {
        List<Map> list = bookMapper.getTable(tableName);
        return list;
    }

    public void saveBook(Map<String, Object> book) {
        if (book == null || book.get("id") == null) {
            throw new ServiceException("保存失败");
        }
        bookMapper.saveBook(book);
    }
}
