package cn.cloudbed.mylibrary.operation.controller;

import cn.cloudbed.common.entity.JsonResult;
import cn.cloudbed.mylibrary.operation.service.ServerOpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/op/server")
public class ServerOpController {
    @Autowired
    private ServerOpService serverOpService;
    /**
     * 显示文件夹中的文件列表
     * @param path
     * @return
     */
    @GetMapping("/ls/{path}")
    @CrossOrigin
    public JsonResult ls(@PathVariable("path") String path){
        Object result= serverOpService.ls(path);
        return new JsonResult(result);
    }

    @GetMapping("/pdf/image/cache/{filepath}/{number}")
    @CrossOrigin
    public JsonResult cacheImagePdf(
            @PathVariable("filepath") String filepath,
            @PathVariable("number") String number){
        Object result=serverOpService.cacheImagePdf(number,filepath);
        return new JsonResult(result);
    }
    @GetMapping("pdf/setcover")
    @CrossOrigin
    public JsonResult setCover(@RequestParam("id") String id,
                               @RequestParam("covera") String covera){
        Object result=serverOpService.setCover(id,covera);
        return new JsonResult(result);
    }

}
