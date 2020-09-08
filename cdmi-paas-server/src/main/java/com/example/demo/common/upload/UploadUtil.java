package com.example.demo.common.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
public class UploadUtil {

    @Autowired
    private UploadConfig uploadConfig;

    public void saveFile(MultipartFile multipartFile, String fileName) throws IOException {
        File file = new File(uploadConfig.getFilePath());
        if (!file.exists()) {
            file.mkdirs();
        }
        FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
        String path = uploadConfig.getFilePath() + File.separator + fileName;
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
        byte[] bs = new byte[1024];
        int len;
        while ((len = fileInputStream.read(bs)) != -1) {
            bos.write(bs, 0, len);
        }
        bos.flush();
        bos.close();
    }

}
