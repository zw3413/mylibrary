package cn.cloudbed.mylibrary.operation.controller;

import cn.cloudbed.common.entity.JsonResult;
import cn.cloudbed.mylibrary.operation.service.GhostOpService;
import cn.cloudbed.operation.GhostAdminOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/op/ghost")
public class GhostOpController {

    private static Boolean removeAllPostsFlag=false;
    private static Boolean pulishAllFlag=false;

    @Autowired
    private GhostOpService ghostOpService;

    @DeleteMapping("/deletePost/{postid}")
    @CrossOrigin
    public JsonResult deletePost(@PathVariable("postid") String postId){
       try {
           ghostOpService.deletePost(postId);
           return new JsonResult();
       }catch(Exception e){
           return new JsonResult(e.getMessage());
       }
    }

    @GetMapping("/republish/{id}")
    @CrossOrigin
    public JsonResult republish(@PathVariable("id") String bookid){

        try{
            ghostOpService.republish(bookid);
            return new JsonResult();
        }catch (Exception e){

            return new JsonResult(e.getMessage());

        }

    }

    /**
     * 操作ghost，移除所有library的文章
     * @return
     */
    @GetMapping("/posts/removeAll")
    @CrossOrigin
    public JsonResult removeAllPosts(){
        Boolean flag=removeAllPostsFlag;
        if(!flag) {
            try {
                flag=true;
                GhostAdminOperation.removeAllLibraryPosts1();
            }catch (Exception e){
                return new JsonResult(e.getMessage());
            }finally {
                flag=false;
            }
            return new JsonResult();
        }else{
            return new JsonResult("正在删除中，请稍后再试...");
        }
    }

    /**
     * 将目前Mlb_book表中的所有文章发布出去
     * @return
     */
    @GetMapping("/posts/publishAll")
    @CrossOrigin
    public JsonResult publishAll(){
        Boolean flag=pulishAllFlag;
        if(!flag) {
            try {
                flag=true;
                GhostAdminOperation.generatePosts();
            }catch (Exception e){
                return new JsonResult(e.getMessage());
            }finally {
                flag=false;
            }
            return new JsonResult();
        }else{
            return new JsonResult("正在删除中，请稍后再试...");
        }
    }




}
