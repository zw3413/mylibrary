package cn.cloudbed.common.client.ghost;

import cn.cloudbed.entity.post.Post;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * feign服务接口
 */
public interface GhostJWTService {
    /**
     * 获取所有post
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET /ghost/api/v2/admin/posts")
    Map<String,Object> getPosts();

    /**
     * 根据id获取post
     * @param id
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET /ghost/api/v2/admin/posts/{id}/")
    Post getPostById(@Param("id") String id);

    /**
     * 根据tag获取post
     * @param slug
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET /ghost/api/v2/admin/posts/slug/{slug}/")
    Map<String,Object> getPostsBySlug(@Param("slug") String slug);

    /**
     * 创建post
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /ghost/api/v2/admin/posts")
    Object createPost(Map<String,Object> requestBody);


    /**
     * 创建post(HTML格式的)
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /ghost/api/v2/admin/posts?source=html")
    Object createPostWithHtml(Map<String,Object> requestBody);


    //@Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("DELETE /ghost/api/v2/admin/posts/{id}/")
    Object deletePost(@Param("id") String id);
}