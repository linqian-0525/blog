package com.lq.blog.controller.admin;

import com.lq.blog.dto.FileDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {
    @Value("${web.upload-path}")
    private String uploadPath;
    @RequestMapping(value = "/file/upload", produces = "application/json;charset=utf-8")
    @ResponseBody
    public FileDTO uploadImages(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file,
                                HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getContextPath());
        String realPath = uploadPath;
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        int radom = (int)(Math.random()*10000);
        String url =  radom+".jpg";
        try {
            /*            file.transferTo(targetFile);*/
            byte[] bytes = file.getBytes();
            Path path = Paths.get(realPath + file.getOriginalFilename());
            System.out.println(path);
            Files.write(path, bytes);

        //  File f = new File("D:/pic.jpg");
           File f = new File("D:/"+fileName);

         //String url =  realPath+"/"+radom+".jpg";
            FileCopyUtils.copy(f, new File(realPath+"/"+url));
            File ff = new File(realPath);
            for(File tt : ff.listFiles()) {
                // System.out.println("fileName : "+tt.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("http://localhost:8887/"+url);
        return fileDTO;
    }
}
