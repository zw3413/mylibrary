package cn.cloudbed.mylibrary.operation.service;

import cn.cloudbed.common.entity.JsonResult;
import cn.cloudbed.common.exception.ServiceException;
import cn.cloudbed.mylibrary.operation.dao.ServerOpDao;
import cn.cloudbed.mylibrary.rest.dao.FileDao;
import cn.cloudbed.operation.DocumentProcessOperation;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ServerOpService {
    Logger logger= LoggerFactory.getLogger(this.getClass().getCanonicalName());

    @Value("${mylibrary.fileBasePath}")
    private String fileBasePath;

    @Autowired
    private ServerOpDao serverOpDao;

    public JsonResult ls(String path) {
        return null;
    }

    public JsonResult cacheImagePdf(String number, String filePath) {

        try {
            if(number == null) throw new ServiceException("参数错误");
            if(filePath==null) throw new ServiceException("参数错误");

            String pdfAbsolutePath=fileBasePath+filePath;
            String imgCachePath = pdfAbsolutePath.split("\\.")[0];
            File imgeCacheFolder = new File(imgCachePath);
            if (imgeCacheFolder.exists()) {
                FileUtil.deleteContents(imgeCacheFolder);
                imgeCacheFolder.delete();
            }
            imgeCacheFolder.mkdirs();

            File file = new File(pdfAbsolutePath);
            Integer endPage = Integer.parseInt(number);
            DocumentProcessOperation.generateImageCache(file, imgCachePath, 1, endPage);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(e.getMessage());
        }


    }

    public Object setCover(String id, String covera) {

       // serverOpDao.setVo
        return new JsonResult();
    }
}
