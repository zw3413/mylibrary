package cn.cloudbed.operation;

import cn.cloudbed.common.client.ghost.GhostJWTClient;
import cn.cloudbed.common.client.ghost.GhostJWTService;
import cn.cloudbed.common.client.ghost.accessories.GhostJDBCTemplate;
import cn.cloudbed.common.util.FileUtil;
import cn.cloudbed.entity.post.Author;
import cn.cloudbed.entity.post.Post;
import cn.cloudbed.entity.post.Tag;
import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 根据book数据库内容，自动发布为ghost系统的post
 */
public class GhostAdminOperation {

    private static String htmlTemplate;

    public static void main(String[] args) {
        try {
            //删掉所有自动发布的post
            removeAllLibraryPosts1();
            //执行自动发布
            generatePosts();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getGhostToken() {
        String token = GhostJWTClient.generateToken(GhostJWTClient.adminKey);
        System.out.println(token);
    }

    /**
     * 使用ghost api删除所有已发布的文章
     */
    private static void removeAllLibraryPosts() {
        //获取ghost接口服务
        GhostJWTService ghostJWTService = GhostJWTClient.getGhostJWTService();
        String sql = "select id from mlb_book";
        JdbcTemplate mysqlJdbcTemplate = GhostJDBCTemplate.getMysqlJdbcTemplate();
        List<Map<String, Object>> list = mysqlJdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : list) {
            String id = (String) map.get("id");
            try {
                ghostJWTService.deletePost(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 直接在数据库删除已经发布的library posts
     */
    private static void removeAllLibraryPosts1(){
        JdbcTemplate mysqlJdbcTemplate = GhostJDBCTemplate.getMysqlJdbcTemplate();
        String sql= " delete from posts_authors where author_id= (select id from users where name ='library'); ";
        mysqlJdbcTemplate.execute(sql);
        sql=" delete from posts_tags where post_id in (select id from posts where author_id= (select id from users where name ='library')); ";
        mysqlJdbcTemplate.execute(sql);
        sql=" delete from posts where author_id =  (select id from users where name ='library') ";
        mysqlJdbcTemplate.execute(sql);

    }

    public static void generatePosts() {
        try {
            //获取ghost接口服务
            GhostJWTService ghostJWTService = GhostJWTClient.getGhostJWTService();
            //获取源数据的数据库连接
            JdbcTemplate mysqlJdbcTemplate = GhostJDBCTemplate.getMysqlJdbcTemplate();

            //每次生成10篇post
            int page = 1;
            int pageSize = 1;
            int offset = pageSize * (page - 1);

            String fetchSql = "select * from mlb_book b left join mlb_book_file f on f.id=b.fileid limit ? offset ? ";
            List<Map<String, Object>> list = mysqlJdbcTemplate.queryForList(fetchSql, new Object[]{pageSize, offset});

            while (list.size() > 0 ) {
                generatePosts(list);
                page++;
                offset = pageSize * (page - 1);
                list = mysqlJdbcTemplate.queryForList(fetchSql, new Object[]{pageSize, offset});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发布post，自动发布
     *
     * @throws JsonProcessingException
     */
    static void generatePosts(List<Map<String,Object>> list) throws JsonProcessingException {
        //获取一个Ghost Admin feign服务
        GhostJWTService ghostJWTService = GhostJWTClient.getGhostJWTService();

        //作者
        Author auth = new Author();
        auth.setId("2");//post的作者id为2
        auth.setName("library"); //post的作者名为library
        List<Author> authors = new ArrayList<>();//多作者
        authors.add(auth);
        //tag
        Tag t = new Tag();
        //tag.setId("library");//tag的id为library
        t.setName("#library");//tag的名字为library
        //tag.setSlug("library");
        List<Tag> tags = new ArrayList<>();//多tag
        tags.add(t);

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> requestBodyMap = new HashMap<>();
            List<Post> posts = new ArrayList<>();
            try {
                Map<String, Object> book = list.get(i);
                Post post = new Post();
                post.setAuthors(authors);
                post.setPrimary_author(auth);//设置第一作者
                post.setTags(tags);//
                post.setPrimary_tag(t); //设置第一tag
                post.setCustom_template("custom-library");
                String imagePath="http://139.196.142.70/file/mylibrary/book/"+book.get("covera");
                post.setFeature_image(imagePath);

                //文章正文
                post.setId((String) book.get("id"));//post的id
                post.setTitle((String) book.get("bookname"));//post的名称

                String html = resolveBookToHtml(book);
                //post.setHtml(html);

                Map<String, Object> mobiledoc = new HashMap<>();
                mobiledoc.put("version", "0.3.1");
                List markups=new ArrayList();
                List markup=new ArrayList();
                markup.add("b");
                markups.add(markup);
                markup=new ArrayList();
                markups.add(markup);
                mobiledoc.put("markups", markups);

                mobiledoc.put("atoms", new ArrayList<>());

                List cards = new ArrayList();
                List card ;
                Map<String,Object> cardObj;



                card=new ArrayList();
                card.add("html");
                cardObj = new HashMap<>();
                cardObj.put("cardName", "html");
                cardObj.put("html", html);
                card.add(cardObj);
                cards.add(card);

                card=new ArrayList();
                card.add("html");
                cardObj = new HashMap<>();
                cardObj.put("cardName","html");
                String filePath=(String)book.get("filepath");
                String h=" <iframe style=\"width: 100%;height:750px\" " +
                        "  src=\"http://139.196.142.70/pdf/web/viewer.html?file=http://139.196.142.70/file/mylibrary/book/"+filePath +" \"/> ";
                cardObj.put("html",h);
                card.add(cardObj);
                cards.add(card);

                card=new ArrayList();
                card.add("image");
                cardObj = new HashMap<>();
                cardObj.put("src", imagePath);
                card.add(cardObj);
                cards.add(card);

                mobiledoc.put("cards", cards);

                List sections = new ArrayList();
                List section ;

//                section =new ArrayList();
//                section.add(10);
//                section.add(2);
//                sections.add(section);

                section =new ArrayList();
                section.add(10);
                section.add(0);
                sections.add(section);

                section=new ArrayList();
                section.add(10);
                section.add(1);
                sections.add(section);
                mobiledoc.put("sections", sections);

                String json = JSONUtils.toJSONString(mobiledoc);
                //System.out.println(json);
                post.setMobiledoc(json);

                post.setStatus("published");
                posts.add(post);

            } catch (IndexOutOfBoundsException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            requestBodyMap.put("posts", posts);
            //  ghostJWTService.createPostWithHtml(requestBodyMap);
            ghostJWTService.createPost(requestBodyMap);
        }


    }

    /**
     *
     */
    public static String resolveBookToHtml(Map<String, Object> book) throws Exception {
        if (htmlTemplate == null) {
            htmlTemplate = FileUtil.getTemplateContentFromClasspath("ghostPostTemplate.html");
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
        html = html.replace("${id}", id)
                .replace("${bookname}", bookname)
                .replace("${publishhouse}", publishhouse)
                .replace("${publishtime}", publishtime)
                .replace("${author}", author)
                .replace("${classification}", classification)
                .replace("${tag}", tag)
                .replace("${brief}", brief)
                .replace("${catalogue}", catalogue)
                .replace("${ratings}", ratings)
                .replace("${covera}", covera)
                .replace("${filepath}", filepath);

        //html.replaceAll
        return html;
    }

}
