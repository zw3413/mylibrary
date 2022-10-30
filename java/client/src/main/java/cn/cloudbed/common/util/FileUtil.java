package cn.cloudbed.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    /**
     * 根据文件名，获取文件后缀
     * @param fileName
     * @return
     */
    public static String getFileAfterfix(String fileName) {
        String afterfix = "unknown";
        if (fileName.contains(".")) {
            String[] split = fileName.split("\\.");
            String lastPart = split[split.length - 1];
            if (lastPart != null && lastPart.length() > 0) {
                afterfix = lastPart;
            }
        }
        return afterfix;
    }


    /**
     * 读取文件中的字符串
     * @return
     * @throws Exception
     */
    public static String getTemplateContent(String path) throws Exception{
        File file = new File(path);
       return getTemplateContent(file);
    }

    /**
     * 读取文件中的字符串
     * @return
     * @throws Exception
     */
    public static String getTemplateContent(File file) throws Exception{
        if(!file.exists()){
            return null;
        }
        FileInputStream inputStream = new FileInputStream(file);
       return getTemplateContent(inputStream);
    }

    /**
     * 读取文件中的字符串
     * @return
     * @throws Exception
     */
    public static String getTemplateContent(InputStream inputStream) throws Exception{
        if(inputStream==null){
            return null;
        }

        int length = inputStream.available();
        byte bytes[] = new byte[length];
        inputStream.read(bytes);
        inputStream.close();
        String str =new String(bytes, StandardCharsets.UTF_8);
        return str ;
    }
    /**
     * 读取类加载路径中的文件中的字符串
     * @return
     * @throws Exception
     */
    public static String getTemplateContentFromClasspath(String filename) throws Exception{
        //String path=FileUtil.class.getClassLoader().getResource(filename).getFile();
        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(filename);
        return getTemplateContent(inputStream);
    }
}
