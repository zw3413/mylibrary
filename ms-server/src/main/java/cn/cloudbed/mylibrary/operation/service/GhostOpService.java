package cn.cloudbed.mylibrary.operation.service;

import cn.cloudbed.common.exception.ServiceException;
import cn.cloudbed.mylibrary.operation.dao.GhostOpDao;
import cn.cloudbed.mylibrary.rest.service.BookService;
import cn.cloudbed.operation.GhostAdminOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GhostOpService {

    private Logger logger= LoggerFactory.getLogger(this.getClass().getCanonicalName());

    @Autowired
    GhostOpDao ghostOpDao;

    @Autowired
    BookService bookService;


    public void republish(String bookid) {

        try{
            String postid = getPostidByBookid(bookid);
            GhostAdminOperation.republishPost(postid,bookid);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    private String getPostidByBookid(String bookid) {
        Map<String,Object> book=bookService.getBookById(bookid);
        return (String) book.get("postid");
    }

    public void deletePost(String postId) {
        GhostAdminOperation.removePost(postId);
    }
}
