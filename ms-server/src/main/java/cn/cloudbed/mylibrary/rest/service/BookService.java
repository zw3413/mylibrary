package cn.cloudbed.mylibrary.rest.service;

import cn.cloudbed.common.exception.ServiceException;
import cn.cloudbed.mylibrary.rest.dao.BookDao;
import cn.cloudbed.operation.GhostAdminOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service("bookService")
public class BookService {

    @Autowired
    private BookDao bookDao;

    Logger logger= LoggerFactory.getLogger(this.getClass().getCanonicalName());

    public Object book_v2(String pageSize, String currentPage, String bookName) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize", pageSize);
        params.put("page", currentPage);
        if (bookName != null && bookName.length() > 0) params.put("name", bookName);
        List bookList = (List) book(params);

        Integer total = bookTotal(params);
        Map<String, Object> map = new HashMap<>();
        map.put("items", bookList);
        map.put("count", total);
        return map;
    }

    private Integer bookTotal(Map params) {
        return bookDao.bookTotal(params);
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
            Integer pageSize = Integer.parseInt((String) param.get(key));
            key = "page";
            if (param.get(key) == null) param.put(key, "1");
            Integer page = Integer.parseInt((String) param.get(key));

            param.put("offset", pageSize * (page - 1));

            Object books = bookDao.queryBook(param);
            return books;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * create ot update a book record, and publish the book to ghost
     *
     * @param book
     */

    public void saveBook(Map<String, Object> book) {

        try {
            if (book == null) throw new ServiceException("parameter fault, no valid object to save.");

            if (book.get("id") == null) {
                book.put("id", UUID.randomUUID().toString());
                bookDao.createBook(book);
            } else {
                bookDao.saveBook(book);
            }

            //publish the ghost post
            String postId=(String)book.get("postid");
            String bookId = (String)book.get("id");
            GhostAdminOperation.republishPost(postId,bookId);

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }

    }

    public List getClassification() {

        return bookDao.getClassification();

    }

    public List getPublishhouse() {
        return bookDao.getPublishhouse();
    }

    public List getAuthor() {
        List<Map> authorList = bookDao.getAuthor();
        Set<String> authorSet = new HashSet<>();
        for (Map authorMap : authorList) {
            if (authorMap == null) continue;
            String authorStr = (String) authorMap.get("value");
            if (authorStr == null) continue;
            String[] arr = authorStr.split("/");
            for (String a : arr) {
                authorSet.add(a);
            }
        }
        List result = new ArrayList();
        for (String a : authorSet) {
            Map map = new HashMap();
            map.put("label", a);
            map.put("value", a);
            result.add(map);
        }
        return result;
    }

    public Map<String, Object> getBookById(String id) {
        Map<String, Object> book = bookDao.getBookById(id);
        return book;
    }
}
