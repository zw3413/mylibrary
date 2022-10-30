package cn.cloudbed.operation;

import cn.cloudbed.common.client.ghost.GhostJWTClient;
import cn.cloudbed.common.client.ghost.GhostJWTService;
import cn.cloudbed.common.client.ghost.accessories.GhostJDBCTemplate;
import cn.cloudbed.common.util.FileUtil;
import cn.cloudbed.common.util.MyStringUtil;
import cn.cloudbed.entity.post.Author;
import cn.cloudbed.entity.post.Post;
import cn.cloudbed.entity.post.Tag;
import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.ServiceException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 根据book数据库内容，自动发布为ghost系统的post
 */
public class GhostAdminOperation {

    private static String htmlTemplate;
    private static int successed;
    private static int failed;

    public static void main(String[] args) {

        //删掉所有自动发布的post
        removeAllLibraryPosts1();
        //执行自动发布
        generatePosts();
     //   republishPost("5e48238ecc1f6700013a6732","003ad64e-b338-4070-8de2-9eaeff11da1d");
    }

    /**
     * 重新将book记录，发布为ghost post
     * @param postId
     * @param bookId
     */
    public static void republishPost(String postId, String bookId) {
        try {
            removePost(postId);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            publishPost(bookId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 使用ghost api删除所有已发布的文章
     */
    public static void removePost(String postid) {
        try {
//            JdbcTemplate mysqlJdbcTemplate = GhostJDBCTemplate.getMysqlJdbcTemplate();
//            //TODO
//            String sql;
//            sql = " delete from posts_tags where post_id = '" + postid + "'";
//            mysqlJdbcTemplate.execute(sql);
//            sql = " delete from posts where id ='" + postid + "'";
//            mysqlJdbcTemplate.execute(sql);
            GhostJWTService ghostJWTService = GhostJWTClient.getGhostJWTService(true);
            ghostJWTService.deletePost(postid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 直接在数据库删除已经发布的library posts
     */
    public static void removeAllLibraryPosts1() {
        JdbcTemplate mysqlJdbcTemplate = GhostJDBCTemplate.getMysqlJdbcTemplate();
        String sql = " delete from posts_authors where author_id= (select id from users where name ='library'); ";
        mysqlJdbcTemplate.execute(sql);
        sql = " delete from posts_tags where post_id in (select id from posts where author_id= (select id from users where name ='library')); ";
        mysqlJdbcTemplate.execute(sql);
        sql = " delete from posts where author_id =  (select id from users where name ='library') ";
        mysqlJdbcTemplate.execute(sql);
        System.out.println("deleted all library posts");

    }

    /**
     * publish a post for one book record
     *
     * @param bookid
     */
    public static void publishPost(String bookid) {
        try {

            //获取源数据的数据库连接
            JdbcTemplate mysqlJdbcTemplate = GhostJDBCTemplate.getMysqlJdbcTemplate();

            String fetchSql = "select * from mlb_book b left join mlb_file f on f.id=b.fileid where b.id= ?";
            List<Map<String, Object>> list = mysqlJdbcTemplate.queryForList(fetchSql, new Object[]{bookid});

            if (list.size() > 0) {
                generatePosts(list);
            } else {
                throw new ServiceException("没有这个记录，发布失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * publish all book records as post to ghost
     */
    public static void generatePosts() {
        successed = 0;
        failed = 0;
        //获取ghost接口服务
        GhostJWTService ghostJWTService = GhostJWTClient.getGhostJWTService();
        //获取源数据的数据库连接
        JdbcTemplate mysqlJdbcTemplate = GhostJDBCTemplate.getMysqlJdbcTemplate();

        //每次生成10篇post
        int page = 1;
        int pageSize = 1;
        int offset = pageSize * (page - 1);

        String fetchSql = "select * from mlb_book b left join mlb_file f on f.id=b.fileid limit ? offset ? ";
        List<Map<String, Object>> list = mysqlJdbcTemplate.queryForList(fetchSql, new Object[]{pageSize, offset});

        while (list.size() > 0) {
            try {
                generatePosts(list);
                page++;
                offset = pageSize * (page - 1);
                list = mysqlJdbcTemplate.queryForList(fetchSql, new Object[]{pageSize, offset});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("published success:" + successed);
        System.out.println("published failed:" + failed);

    }


    /**
     * 发布post，自动发布
     *
     * @param list
     * @throws JsonProcessingException
     */
    static void generatePosts(List<Map<String, Object>> list) throws Exception {

        //获取一个Ghost Admin feign服务
        GhostJWTService ghostJWTService = GhostJWTClient.getGhostJWTService();

        //作者
        Author auth = new Author();
        auth.setId("2");//post的作者id为2
        auth.setName("library"); //post的作者名为library
        List<Author> authors = new ArrayList<>();//多作者
        authors.add(auth);


        for (int i = 0; i < list.size(); i++) {
            String bookname = "";
            try {
                Map<String, Object> book = list.get(i);
                Post post = new Post();
                post.setAuthors(authors);
                post.setPrimary_author(auth);//设置第一作者

                post.setCustom_template("custom-library");
                String imagePath = "http://139.196.142.70/file/mylibrary/book/" + book.get("covera");
                post.setFeature_image(imagePath);

                //tag
                Tag t = new Tag();
                //tag.setId("library");//tag的id为library
                t.setName("#library");//tag的名字为library
                post.setPrimary_tag(t); //设置第一tag
                //tag.setSlug("library");
                List<Tag> tags = new ArrayList<>();//多tag
                tags.add(t);
                String bookTag = (String) book.get("tag");
                if (bookTag != null) {
                    if (bookTag.contains("/")) {
                        for (String t1 : bookTag.split("/")) {
                            t = new Tag();
                            t.setName(t1);
                            tags.add(t);
                        }
                    } else {
                        t = new Tag();
                        t.setName(bookTag);
                        tags.add(t);
                    }
                }
                post.setTags(tags);//
                //文章正文
                post.setId((String) book.get("id"));//post的id
                post.setTitle((String) book.get("bookname"));//post的名称
                bookname = (String) book.get("bookname");
                String html = resolveBookToHtml(book);
                //post.setHtml(html);

                Map<String, Object> mobiledoc = new HashMap<>();
                mobiledoc.put("version", "0.3.1");
                List markups = new ArrayList();
                List markup = new ArrayList();
                markup.add("b");
                markups.add(markup);
                markup = new ArrayList();
                markups.add(markup);
                mobiledoc.put("markups", markups);
                mobiledoc.put("atoms", new ArrayList<>());

                List cards = new ArrayList();
                List card;
                Map<String, Object> cardObj;

                card = new ArrayList();
                card.add("html");
                cardObj = new HashMap<>();
                cardObj.put("cardName", "html");
               html=MyStringUtil.removeRN(html);
                cardObj.put("html", html);
                card.add(cardObj);
                cards.add(card);

                card = new ArrayList();
                card.add("html");
                cardObj = new HashMap<>();
                cardObj.put("cardName", "html");
                String filePath = (String) book.get("filepath");
                String h = " <iframe style=\"width: 100%;height:750px\" " +
                        "  src=\"http://139.196.142.70/pdf/web/viewer.html?file=http://139.196.142.70/file/mylibrary/book/" + filePath + " \"/> ";

                h=MyStringUtil.removeRN(h);
                cardObj.put("html", h);
                card.add(cardObj);
                cards.add(card);

                card = new ArrayList();
                card.add("image");
                cardObj = new HashMap<>();
                cardObj.put("src", imagePath);
                card.add(cardObj);
                cards.add(card);

                mobiledoc.put("cards", cards);

                List sections = new ArrayList();
                List section = new ArrayList();
                section.add(10);
                section.add(0);
                sections.add(section);

                section = new ArrayList();
                section.add(10);
                section.add(1);
                //sections.add(section);
                mobiledoc.put("sections", sections);

                String json = JSONUtils.toJSONString(mobiledoc);
                //System.out.println(json);
                post.setMobiledoc(json);

                post.setStatus("published");
                Map<String, Object> requestBodyMap = new HashMap<>();
                List<Post> posts = new ArrayList<>();
                posts.add(post);

                requestBodyMap.put("posts", posts);
                //  ghostJWTService.createPostWithHtml(requestBodyMap);
                if((successed+failed)%500  == 0 ){
                    ghostJWTService=GhostJWTClient.getGhostJWTService(true);
                }
                Map<String, List<Map<String, Object>>> result = (Map) ghostJWTService.createPost(requestBodyMap);
                String postId = (String) ((Map<String, List<Map<String, Object>>>) result).get("posts").get(0).get("id");
                savePostId((String) book.get("id"), postId);
                successed++;
            } catch (Exception e) {
                failed++;
                System.out.println(LocalTime.now()+"  published failed " + failed + "(successed " + successed + "):" + bookname);
                e.printStackTrace();
            }

        }


//        List<Map<String, Object>> resultPosts = result.get("posts");
//
//        for (int i = 0; i < resultPosts.size(); i++) {
//            String postId = (String) ((Map<String, Object>) resultPosts.get(i)).get("id");
//            String postTitle = (String) ((Map<String, Object>) resultPosts.get(i)).get("title");
//            for (int j = 0; j < list.size(); j++) {
//                String bookName = (String) list.get(i).get("bookname");
//                if (postTitle.equals(bookName)) {
//                    savePostId((String) list.get(j).get("id"), postId);
//                }
//            }
//        }


    }

    /**
     * update the postid for a just published book record
     *
     * @param id
     * @param postId
     */
    private static void savePostId(String id, String postId) {
        try {
            JdbcTemplate mysqlJdbcTemplate = GhostJDBCTemplate.getMysqlJdbcTemplate();
            String fetchSql = "update mlb_book set postid= ? where id= ?";
            mysqlJdbcTemplate.update(fetchSql, new Object[]{postId, id});
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * generate a book map to html content, for html type source post
     *
     * @param book
     * @return
     * @throws Exception
     */
    public static String resolveBookToHtml(Map<String, Object> book) throws Exception {

        if (htmlTemplate == null) {
        try {
            htmlTemplate = FileUtil.getTemplateContentFromClasspath("ghostPostTemplate.html");
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        }
        String html = htmlTemplate;
        String id = book.get("id") != null ? book.get("id").toString() : "";
        String bookname = book.get("bookname") != null ? book.get("bookname").toString() : "";
        String publishhouse = book.get("publishhouse") != null ? book.get("publishhouse").toString() : "";
        String publishtime = book.get("publishtime") != null ? book.get("publishtime").toString() : "";
        String author = book.get("author") != null ? book.get("author").toString() : "";
        String classification = book.get("classification") != null ? book.get("classification").toString() : "";
        String tag = book.get("tag") != null ? book.get("tag").toString() : "";
        String catalogue = book.get("catalogue") != null ? book.get("catalogue").toString() : "";
        String brief = book.get("brief") != null ? book.get("brief").toString() : "";
        String ratings = book.get("ratings") != null ? book.get("ratings").toString() : "";
        String covera = book.get("covera") != null ? book.get("covera").toString() : "";
        String filepath = book.get("filepath") != null ? book.get("filepath").toString() : "";
        String fileid = book.get("fileid") != null ? book.get("fileid").toString() : "";
        System.out.println("开始替换参数");
        System.out.println("1."+id+" 2."+bookname+" 3."+publishhouse+" 4."+publishtime+" 5."+author+" 6."+classification+" 7."+
                tag+" 8."+catalogue+" 9."+brief+" 10."+ratings+" 11."+covera+" 12."+filepath+" 13."+fileid);
        System.out.println(html);
        html = html.replace("${id}", id);
        html = html.replace("${bookname}", bookname);
        html = html.replace("${publishhouse}", publishhouse);
        html = html.replace("${publishtime}", publishtime);
        html = html.replace("${author}", author);
        html = html.replace("${classification}", classification);
        html = html.replace("${tag}", tag);
        html = html.replace("${brief}", brief);
        html = html.replace("${catalogue}", catalogue);
        html = html.replace("${ratings}", ratings);
        html = html.replace("${covera}", covera);
        html = html.replace("${filepath}", filepath);
        html = html.replace("${fileid}", fileid);
        System.out.println("结束替换参数");
        //html.replaceAll
        return html;
    }


}
