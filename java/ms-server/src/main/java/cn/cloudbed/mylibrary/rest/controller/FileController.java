package cn.cloudbed.mylibrary.rest.controller;

import cn.cloudbed.common.entity.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private cn.cloudbed.mylibrary.rest.service.FileService fileService;

    @Value("${mylibrary.fileBasePath}")
    private String fileBasePath;
    @Value("${mylibrary.fileBaseUrl}")
    private String fileBaseUrl;
    @Value("${mylibrary.pdfViewerUrl}")
    private String pdfViewerUrl;

    @GetMapping(value="/getFileById")
    @CrossOrigin
    public JsonResult getFileById(@RequestParam("id") String id){

        try {
            Object result = fileService.getFile(id);
            return new JsonResult(result);
        }catch (Exception e){
            return new JsonResult(e.getMessage());
        }
    }

    @GetMapping(value="/basepath")
    @CrossOrigin
    public JsonResult getBasePath(){
        return new JsonResult(fileBasePath);
    }

    @GetMapping(value="/baseurl")
    @CrossOrigin
    public JsonResult getBaseUrl(){
        return new JsonResult(fileBaseUrl);
    }

    @GetMapping(value="/pdfviewer")
    @CrossOrigin
    public JsonResult getPdfviewer(){
        return new JsonResult(pdfViewerUrl);
    }

    /**
     * 获取列表
     * @param pageSize
     * @param currentPage
     * @param name
     * @return
     */
    @GetMapping(value="/{pageSize}/{currentPage}")
    @CrossOrigin
    public JsonResult getFile(@PathVariable("pageSize") String pageSize,
                               @PathVariable("currentPage") String currentPage,
                               @RequestParam(value = "name",required = false) String name){

        Object result= fileService.getFile(pageSize,currentPage,name);
        JsonResult jsonResult=new JsonResult(result);
        return jsonResult;
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @GetMapping(value="/{id}")
    @CrossOrigin
    public JsonResult getFile(@PathVariable("id") String id){
        Object result= fileService.getFile(id);
        return new JsonResult(result);
    }

    /**
     * 更新
     * @param id
     * @param fileMap
     * @return
     */
    @PutMapping(value="/{id}")
    @CrossOrigin
    public JsonResult updateFile(@PathVariable("id") String id,
                                 @RequestBody Map<String,Object> fileMap ){

        Object result= fileService.updateFile(id,fileMap);
        JsonResult jsonResult=new JsonResult(result);
        return jsonResult;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping(value="/{id}")
    @CrossOrigin
    public JsonResult deleteFile(@PathVariable("id") String id){

        Object result= fileService.deleteFile(id);

        return new JsonResult(result);
    }



}
