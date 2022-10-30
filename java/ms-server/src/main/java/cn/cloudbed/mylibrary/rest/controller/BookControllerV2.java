package cn.cloudbed.mylibrary.rest.controller;

import cn.cloudbed.common.entity.JsonResult;
import cn.cloudbed.mylibrary.rest.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.Map;

@RestController
@RequestMapping("/v2/book")
public class BookControllerV2 {

    @Autowired
    private BookService bookService;



    @GetMapping(value = "{pageSize}/{currentPage}")
    @CrossOrigin
    public Object book_v2(@PathVariable("pageSize") String pageSize,
                          @PathVariable("currentPage") String currentPage,
                          @RequestParam(value = "bookName", required = false) String bookName
    ) {

        Object data = bookService.book_v2(pageSize, currentPage, bookName);
        JsonResult jsonResult = new JsonResult(data);
        return jsonResult;
    }


    @GetMapping("/classification")
    @CrossOrigin
    public JsonResult getClassification() {
        Object result = bookService.getClassification();
        return new JsonResult(result);
    }

    @GetMapping("/publishhouse")
    @CrossOrigin
    public JsonResult getPublishhouse() {
        Object result=bookService.getPublishhouse();
        return new JsonResult(result);
    }

    @GetMapping("/author")
    @CrossOrigin
    public JsonResult getAuthor(){
        Object result=bookService.getAuthor();
        return new JsonResult(result);
    }

}
