package com.bianyi.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
public class UploadService {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg", "image/gif","application/x-jpg","image/png");

    /**
     * 文件上传
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        //校验文件类型
        String type = file.getContentType();
        if (!CONTENT_TYPES.contains(type)){
            return null;
        }
        //校验文件内容
        try {
            BufferedImage read = ImageIO.read(file.getInputStream());
            if (read==null){
                return null;
            }
            /*//保存到服务器
            file.transferTo(new File("D:\\images\\"+filename));
            return "http://image.bianyi.com/" + filename;*/

            String ext = StringUtils.substringAfterLast(filename, ".");
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            // 生成url地址，返回
            return "http://image.bianyi.com/" + storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }
}
