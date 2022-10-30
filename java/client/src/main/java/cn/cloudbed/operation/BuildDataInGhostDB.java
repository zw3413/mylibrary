package cn.cloudbed.operation;

import cn.cloudbed.common.util.DataSourceFactory;
import cn.cloudbed.common.client.ghost.accessories.GhostJDBCTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 将pg数据库中的book相关表迁移到mysql中, 系统早期构建工具，已经不再需要
 */
public class BuildDataInGhostDB {

    private final String remoteIp = "139.196.142.70";
    private JdbcTemplate pg = null;
    private JdbcTemplate mysql = null;

    private void initPg() {
        if (pg == null) {
            DataSource ds=DataSourceFactory.getDataSource(DataSourceFactory.DBTYPE_POSTGRES, remoteIp, "5432", "mylibrary", "postgres", "Good@2020");
            pg = GhostJDBCTemplate.getTemplate(ds);
        }
    }

    private void initMysql() {
        if (mysql == null) {
            DataSource ds=DataSourceFactory.getDataSource(DataSourceFactory.DBTYPE_MYSQL, remoteIp, "3306", "ghost_prod", "root", "Good@2020");
            mysql = GhostJDBCTemplate.getTemplate(ds);
        }
    }

    @Test
    public void testJdbcTemplate() {
        try {
            initPg();
            initMysql();
            pg.queryForList("select * from book");
            mysql.queryForList("select * from mlb_book1");
            System.out.println("数据库连接成功");
        }catch(Exception e){
            System.err.println("数据库连接失败");
            e.printStackTrace();
        }

    }

    /**
     * 重构系统 201908-a
     * 将pg中的book数据迁移到mysql中，并重构数据库结构
     */
    @Test
    public void transferBookFromPgToMysql() {
        //连接两个数据库
        initPg();
        initMysql();
        //1. 获取所有的book
        List<Map<String,Object>> books=pg.queryForList("select * from book");
        //2.将book相应的写入 mlb_book,mlb_file,mlb_publishhouse,mlb_author
        String sql="";
        for(Map<String,Object> book: books){
            try {
                String fileid = uuid();
                String filePath = (String) book.get("localpath");
                String filename = "";
                String format = "";
                if (filePath.indexOf(".") > -1) {

                    filename = filePath.split("\\.")[0];
                    format = filePath.split("\\.")[1];
                } else {
                    filename = filePath;
                    format = null;
                }
                try {
                    filename = new String(filename.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return;
                }
                System.out.println(filePath);
                sql = "insert into mlb_book_file1(id,filename,filesize,filepath,format,createtime,isdel,baidu,wangpan)" +
                        "values(?,?,?,?,?,?,?,?,?)";
                mysql.update(sql, new Object[]{fileid, filename, null, filePath, format, LocalDateTime.now(), 0, null, null});
                String name = (String) book.get("cnname");
                if (StringUtils.isEmpty(name)) {
                    name = (String) book.get("enname");
                }
                String publishhouse = (String) book.get("publisher");
                Date publishtime = (Date) book.get("publicationdate");
                String author = "";
                if (book.get("author1") != null) author += "/" + book.get("author1");
                if (book.get("author2") != null) author += "/" + book.get("author2");
                if (book.get("author3") != null) author += "/" + book.get("author3");
                Integer l = author.length();
                if(l>1) author = author.substring(1, l);

                String classification = "";
                if (book.get("classification1") != null) classification += "/" + book.get("classification1");
                if (book.get("classification2") != null) classification += "/" + book.get("classification2");
                l = classification.length();
                if(l>1) classification = classification.substring(1, l);

                String tag = "";
                if (book.get("tag1") != null) tag += "/" + book.get("tag1");
                if (book.get("tag2") != null) tag += "/" + book.get("tag2");
                l = tag.length();
                if(l>1) tag = tag.substring(1, l);

                String language=null;
                if(!StringUtils.isEmpty((String) book.get("language"))){
                    language=(String) book.get("language");
                }
                sql = "insert into mlb_book1 (id,language, bookname,publishhouse,publishtime,author,classification,tag,fileid,brief,catalogue,covera,coverb,updatetime ) " +
                        "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?  )";
                mysql.update(sql, new Object[]{uuid(),language, name, publishhouse, publishtime, author, classification, tag, fileid, null, null, null, null, LocalDateTime.now()});
            }catch (Exception e){
                System.out.println(book);
                e.printStackTrace();
                throw e;
            }
        }


    }

    private String uuid(){
        return UUID.randomUUID().toString();
    }
    private String empty(){
        return "";
    }



}
