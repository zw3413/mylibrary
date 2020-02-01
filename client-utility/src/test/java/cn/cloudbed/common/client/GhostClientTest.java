package cn.cloudbed.common.client;


import cn.cloudbed.common.client.ghost.GhostJWTClient;
import cn.cloudbed.common.client.ghost.GhostJWTService;
import cn.cloudbed.entity.post.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GhostClientTest {

    private GhostJWTService ghostJWTService;

    @Before
    public void startServer() {
        ghostJWTService = GhostJWTClient.getGhostJWTService();
    }


    @Test
    public void testGetPosts() {

        try {
            Map<String, Object> result = ghostJWTService.getPosts();
            ObjectMapper objectMapper = new ObjectMapper();
            List list = (List) result.get("posts");
            List<Post> postList = new ArrayList();
            for (Object obj : list) {
                Post p = objectMapper.convertValue(obj, Post.class);
                postList.add(p);
            }
            System.out.println(postList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCreatePost() {

        try {
            Post post = new Post();
            post.setId("20191019001");
            post.setTitle("从客户端创建的日志3");
            post.setHtml("<html><div style=\"color:red\">从客户端创建的日志2</div></html>");
            post.setMobiledoc("[测试创建日志3]");
            List<Post> posts = new ArrayList<Post>();
            posts.add(post);
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("posts", posts);
            ghostJWTService.createPost(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetPostById() {

        try {
            Post post = ghostJWTService.getPostById("20191019001");
            System.out.println(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
