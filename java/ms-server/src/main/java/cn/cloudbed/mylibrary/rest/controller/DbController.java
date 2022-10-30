package cn.cloudbed.mylibrary.rest.controller;

import cn.cloudbed.mylibrary.rest.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class DbController {

    @Autowired
    private DbService dbService;


    @GetMapping(value="/table")
    @CrossOrigin
    public Object table(@RequestParam("name") String tableName){

        return dbService.table(tableName);
    }
}
