package cn.cloudbed.mylibrary.book;

import cn.cloudbed.common.entity.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value="/book")
    @CrossOrigin
    public Object book(@RequestBody(required = false) HashMap<String,Object> param){

        return bookService.book(param);
    }

    @GetMapping(value="/v2/book/{pageSize}/{currentPage}")
    @CrossOrigin
    public Object book_v2(@PathVariable("pageSize") String pageSize,
                         @PathVariable("currentPage") String currentPage,
                         @RequestParam(value = "bookName", required = false) String bookName
    ){

        //Integer ps= Integer.parseInt(pageSize);
        //Integer cp=Integer.parseInt(currentPage);
        Object data=bookService.book_v2(pageSize,currentPage,bookName);
        JsonResult jsonResult=new JsonResult(data);
        return jsonResult;
    }

    @GetMapping(value="/table")
    @CrossOrigin
    public Object table(@RequestParam("name") String tableName){

        return bookService.table(tableName);
    }


    @PutMapping(value = "/book")
    @CrossOrigin
    public Object save(@RequestBody Map<String,Object> book){
        bookService.saveBook(book);

        return new JsonResult();
    }

}
