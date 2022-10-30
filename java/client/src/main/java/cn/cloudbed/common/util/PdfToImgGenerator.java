package cn.cloudbed.common.util;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class PdfToImgGenerator {
    /**
     * 从pdf文件中，获取指定页数的图片，用作预览或者封面
     *
     * @param pdf pdf文件
     * @param targetImgPath 生成图片的目标路径
     * @param startPage 起始页码
     * @Param endPage 结束页码
     * @Param scale 图片大小比例
     * @return
     * @throws Exception
     * @Param endPage
     */

    public static void generateJpegFromPdf(File pdf, String targetImgPath, Integer startPage, Integer endPage, Float scale) throws Exception {
        //1. 参数检查
        if (pdf == null || pdf.isDirectory()) throw new Exception("pdf文件不合法");
        if (targetImgPath == null || targetImgPath.length() < 1) throw new Exception("目标路径不合法");
        if (startPage == null || startPage < 1) throw new Exception("起始页码不合法");
        if (endPage == null || endPage < startPage) throw new Exception("结束页码不合法");
        if(scale == null) scale=1.1f;

        //创建pdf解析器
        Document document = new Document();
        try {
            document.setFile(pdf.getAbsolutePath());

            float rotation = 0f;// 旋转角度
            //使用icepdf工具获取页面
            for (int i = startPage-1 ; i < document.getNumberOfPages() && i < endPage; i++) {
                BufferedImage image = (BufferedImage) document.getPageImage(i,
                        GraphicsRenderingHints.SCREEN,
                        org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX,
                        rotation, scale);
                RenderedImage rendImage = image;
                try {
                    File targetImgDir=new File(targetImgPath);
                    boolean targetImgDirExists=targetImgDir.exists();

                    if(!targetImgDirExists){
                        targetImgDir.mkdirs();
                    }
                    File targetImgfile = new File(targetImgPath + File.separator+ (i+1) + ".jpg");
                    boolean targetImgfileExists=targetImgfile.exists();
                    try{
                    if(!targetImgfileExists) {
                        targetImgfile.createNewFile();
                    }}catch (IOException e){
                        throw new IOException("创建img文件失败");
                    }
                    // 这里png作用是：格式是jpg但有png清晰度
                    ImageIO.write(rendImage, "png", targetImgfile);
                    //将生成的文件名返回
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.flush();
            }
            document.dispose();
        } catch (PDFException e1) {
            e1.printStackTrace();
        } catch (PDFSecurityException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
