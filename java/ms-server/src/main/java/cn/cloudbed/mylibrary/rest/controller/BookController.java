package cn.cloudbed.mylibrary.rest.controller;

import cn.cloudbed.common.entity.JsonResult;
import cn.cloudbed.mylibrary.rest.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/book")
    @CrossOrigin
    public Object book(@RequestBody(required = false) HashMap<String, Object> param) {

        return bookService.book(param);
    }


    @PostMapping(value = "/book")
    @CrossOrigin
    public Object save(@RequestBody Map<String, Object> book) {
        bookService.saveBook(book);
        return new JsonResult();
    }


}
