package cn.cloudbed.common.util;

import org.junit.Test;

import java.io.File;

public class PdfToImgGeneratorTest {

    @Test
    public void testpdfToJpeg(){

        File pdf=new File("/Users/weizhang/file/source/sample.pdf");
        String targetPath="/Users/weizhang/file/target";
        try {
            //PdfToImgGenerator.generateJpegFromPdf(pdf, targetPath, 3);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
