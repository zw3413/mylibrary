package cn.cloudbed.operation;

import cn.cloudbed.common.client.ghost.accessories.GhostJDBCTemplate;
import cn.cloudbed.common.util.DataSourceFactory;
import cn.cloudbed.common.util.FileUtil;
import cn.cloudbed.common.util.FtpUtil;
import cn.cloudbed.common.util.PdfToImgGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.DigestUtils;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 对文档的处理
 * 1. 读取pdf和txt格式的文档
 * 2. 将文档匿名并入库
 * 3. 生成文档的预览图片
 */
public class DocumentProcessOperation {

    private static final String remoteIp = "139.196.142.70";
    private static JdbcTemplate postgresTemplate = null;
    private static JdbcTemplate mysqlTemplate = null;
    private static final Logger LOGGER = LoggerFactory.getLogger("DocumentProcessOperation");

    private void initPostgresTemplate() {
        if (postgresTemplate == null) {
            DataSource ds = DataSourceFactory.getDataSource(
                    DataSourceFactory.DBTYPE_POSTGRES,
                    remoteIp,
                    "5432",
                    "mylibrary",
                    "postgres",
                    "Good@2020");
            postgresTemplate = GhostJDBCTemplate.getTemplate(ds);
        }
    }

    private static void initMysqlTemplate() {
        if (mysqlTemplate == null) {
            DataSource ds = DataSourceFactory.getDataSource(
                    DataSourceFactory.DBTYPE_MYSQL, remoteIp,
                    "3306",
                    "library",
                    "root",
                    "Good@2020");
            mysqlTemplate = GhostJDBCTemplate.getTemplate(ds);
        }
    }

    /**
     * 用默认参数启动文档处理
     *
     * @throws Exception
     */
    public static void readDocumentAndProcess() throws Exception {
        LOGGER.debug("::使用默认配置");
        String fileRootPath = "/Users/weizhang/file"; //文件根目录
        String sourcePath = fileRootPath + "/source";//原始文件目录
        String targetPath = fileRootPath + "/target"; //文件目标目录
        String tempPath = fileRootPath + "/temp";//临时目录
        String formats = "pdf,txt";
        readDocumentAndProcess(sourcePath, targetPath, tempPath, formats);
    }

    /**
     * 从原始文件目录中，读取文件，进行处理、生成图片、入库等操作
     *
     * @param sourcePath
     * @param targetPath
     * @param tempPath
     * @param formats
     * @throws Exception
     */
    public static void readDocumentAndProcess(String sourcePath, String targetPath, String tempPath, String formats)
            throws Exception {
        LOGGER.debug("::开始处理文档");
        //1. 参数检查

        //2.准备
        File sourceDir = new File(sourcePath);
        FilenameFilter filenameFilter = (dir, name) -> {
            //定义要读取的文件后缀, pdf/txt
            String afterfix = FileUtil.getFileAfterfix(name);
            String[] formatArr;
            if (formats.contains(",")) {
                formatArr = formats.split(",");
            } else {
                formatArr = new String[]{formats};
            }
            for (String format : formatArr) {
                if (afterfix.equalsIgnoreCase(format))
                    return true;
            }
            return false;
        };
        LOGGER.debug("::设置文件过滤器，只处理pdf,txt文档");
        //3. 获取文件列表，逐一处理文件
        File[] fileList = sourceDir.listFiles(filenameFilter);
        LOGGER.debug("::共找到" + fileList.length + "个待处理文件,开始循环处理这些文件");
        int count = 0;
        int jump = 0;
        int pdfCount=0;
        int txtCount=0;
        int pdfJump=0;
        int txtJump=0;
        for (File file : fileList) {
            LOGGER.debug("::::开始处理文件:" + file.getAbsolutePath());

            //3.2 针对不同的文件类型，执行不同的操作
            String afterfix = FileUtil.getFileAfterfix(file.getName()).toUpperCase();
            boolean jumped = true;
            switch (afterfix) {
                case "PDF": //PDF
                    jumped = !processPDF(file, targetPath);
                    if(jumped){
                        pdfJump++;
                    }else{
                        pdfCount++;
                    }
                    break;
                case "TXT": //TXT
                    jumped = !processTXT(file, targetPath);
                    if(jumped){
                        txtJump++;
                    }else{
                        txtCount++;
                    }
                    break;
            }
            if (jumped) {
                jump++;
            } else {
                count++;
            }
            LOGGER.debug("::::文件处理结束");
        }
        LOGGER.debug("::::主循环结束，共入库和转移文件" + count + "个,跳过文件" + jump + "个");
        LOGGER.debug("发现文件"+fileList.length+"个");
        LOGGER.debug("处理文件"+count+"个,pdf"+pdfCount+"个,txt"+txtCount+"个");
        LOGGER.debug("跳过文件"+jump+"个,pdf"+pdfJump+"个,txt"+txtJump+"个");
    }

    /**
     * 保存mlb_book及 mlb_book_file表
     *
     * @param book
     */
    private static void saveBook(Map<String, Object> book) {
        if (book == null) return;
        initMysqlTemplate();
        //1.保存mlb_book
        String sql = "insert into mlb_book1(id,bookname,fileid,covera,updatetime,filetype,digest) values (?,?,?,?,?,?,?)";
        mysqlTemplate.update(sql,
                new Object[]{
                        book.get("id"),
                        book.get("bookname"),
                        book.get("fileid"),
                        book.get("covera"),
                        LocalDateTime.now(),
                        book.get("filetype"),
                        book.get("digest")
                });
        //2.保存mlb_book_file
        Map<String, Object> file = (Map<String, Object>) book.get("file");
        if (file != null) {
            sql = "insert into mlb_book_file1(id,filename,filesize,filepath,format,createtime,isdel) values(?,?,?,?,?,?,?)";
            mysqlTemplate.update(sql,
                    new Object[]{
                            file.get("id"),
                            file.get("filename"),
                            file.get("filesize"),
                            file.get("filepath"),
                            file.get("format"),
                            LocalDateTime.now(),
                            file.get("isdel")
                    });
        }
    }

    /**
     * 检查文件是否已经在mlb_book表中注册过
     *
     * @param digest
     * @return
     */
    private static boolean checkIfSaved(String digest) throws IOException {
        //String digest=DigestUtils.md5DigestAsHex(new FileInputStream(file));
        initMysqlTemplate();
        String sql = "select id from mlb_book1 where digest = ?";
        List list = mysqlTemplate.queryForList(sql, new Object[]{digest});
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    static void generateCover(File file, String targetImgPath) {
        try {
            PdfToImgGenerator.generateJpegFromPdf(file, targetImgPath, 1, 1, 0.8f);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("生成图片失败");
        }
    }

    /**
     * 处理pdf格式的文件：将pdf文件转移至目标文件夹，用uuid命名，生成cover图
     * ./111111....pdf
     * ./111111.../1.jpeg
     * <p>
     * 并将 uuid、文件名、cover图片路径等存入mlb_book表中
     *
     * @param file
     * @param targetPath
     * @return boolean
     * @throws Exception
     */
    public static boolean processPDF(File file, String targetPath) throws Exception {
        LOGGER.debug("::处理此PDF文档");
        String digest = DigestUtils.md5DigestAsHex(new FileInputStream(file));
        boolean saved = checkIfSaved(digest);
        if (saved) {
            return false;
        }
        //1. 将文件移至目标文件夹
        String afterfix = FileUtil.getFileAfterfix(file.getName()).toUpperCase();
        String uuid = UUID.randomUUID().toString();
        String targetFileName = uuid + "." + afterfix;
        String targetFilePath = targetPath + File.separator + targetFileName;
        transferFile(file, targetFilePath);//将文件存入目标文件夹
        LOGGER.debug("::将此文件转移至目标路径");

        //2. 生成预览图片
        generateCover(file, targetPath + File.separator + uuid);
        LOGGER.debug("::生成封面图片");

        //3. 上传百度网盘
//        String baiduToken="23.77edbd1b5c75a1ac6d40f05809983447.2592000.1575817082.4160989278-17701648";
//        String command=
//                "curl -k -L -F " +
//                "\"file=@"+file.getAbsolutePath()+"\" " +
//                "\"https://c.pcs.baidu.com/rest/2.0/pcs/file?method=upload" +
//                "&access_token=" + baiduToken +
//                "&path=/apps/pcstest_oauth/test/"+file.getName()+"\"";

        //4. 将信息存入数据库mlb_book表
        Map<String, Object> book = new HashMap<>();
        book.put("id", uuid);
        book.put("bookname", file.getName().split("\\.")[0]);
        book.put("fileid", uuid);
        book.put("covera", uuid + File.separator + "1.jpg");
        book.put("updatetime", LocalDateTime.now());
        book.put("filetype", afterfix);
        book.put("digest", digest);

        Map<String, Object> bookFile = new HashMap<>();
        bookFile.put("id", uuid);
        bookFile.put("filename", file.getName().split("\\.")[0]);
        bookFile.put("filesize", file.length());
        bookFile.put("filepath", targetFileName);
        bookFile.put("format", afterfix);
        bookFile.put("createtime", LocalDateTime.now());
        bookFile.put("isdel", 0);
        book.put("file", bookFile);
        saveBook(book);
        LOGGER.debug("::入库文档及文件信息");
        return true;

    }

    public static boolean processTXT(File file, String targetPath) throws Exception {
        LOGGER.debug("::处理此TXT文档");
        String digest = DigestUtils.md5DigestAsHex(new FileInputStream(file));
        boolean saved = checkIfSaved(digest);
        if (saved) {
            return false;
        }
        //1.将txt文件重命名后放到目标路径
        String uuid = UUID.randomUUID().toString();
        String afterfix = FileUtil.getFileAfterfix(file.getName()).toUpperCase();
        String targetFileName = uuid + "." + afterfix;
        String targetFilePath = targetPath + File.separator + targetFileName;
        transferFile(file, targetFilePath);//将文件存入目标文件夹
        LOGGER.debug("::将此文件转移至目标路径");

        //2. 存库
        Map<String, Object> book = new HashMap<>();
        String bookName = file.getName().split("\\.")[0];
        book.put("id", uuid);
        book.put("bookname", bookName);
        book.put("updatetime", LocalDateTime.now());
        book.put("filetype", afterfix);
        book.put("digest", digest);
        book.put("fileid", uuid);

        Map<String, Object> bookFile = new HashMap<>();
        bookFile.put("id", uuid);
        bookFile.put("filename", bookName);
        bookFile.put("filesize", file.length());
        bookFile.put("filepath", targetFileName);
        bookFile.put("format", afterfix);
        bookFile.put("createtime", LocalDateTime.now());
        bookFile.put("isdel", 0);

        book.put("file", bookFile);
        saveBook(book);
        LOGGER.debug("::入库文档及文件信息");
        return true;
    }


    public static void transferFile(File sourceFile, String targetFilePath) throws Exception {
        File targetFile = new File(targetFilePath);
        try {
            if (!targetFile.exists()) targetFile.createNewFile();
        } catch (IOException e) {
            throw new Exception("目标文件夹创建失败", e);
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(targetFile);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fis.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }
            fos.flush();
        } catch (IOException e) {
            throw new Exception("将文件从源文件转移至目标文件出错", e);
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        try {
            readDocumentAndProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
