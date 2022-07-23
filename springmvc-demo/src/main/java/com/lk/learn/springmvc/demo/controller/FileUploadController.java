package com.lk.learn.springmvc.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lk.learn.springmvc.demo.domain.FileDomain;
import com.lk.learn.springmvc.demo.domain.MultiFileDomain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileUploadController {
    // 得到一个用来记录日志的对象，这样在打印信息时能够标记打印的是哪个类的信息
    private static final Log logger = LogFactory.getLog(FileUploadController.class);
    @RequestMapping("getFileUpload")
    public String getFileUpload() {
        return "fileUpload";
    }

    @Value("${spring.servlet.multipart.location}")
//    @Value(value = "${spring.servlet.multipart.location}")
    private String fileUploadLoc;

    /**
     * 单文件上传
     *
     *
     必须要指定 可以直接访问的 临时文件存储位置 ？
     java.io.IOException: java.io.FileNotFoundException: C:\Users\admin\AppData\Local\Temp\tomcat-docbase.1899634146945745141.8080\\uploadfiles\xd身份证_20220701100020.jpg (拒绝访问。)
     at org.apache.catalina.core.ApplicationPart.write(ApplicationPart.java:122)
     at org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile.transferTo(StandardMultipartHttpServletRequest.java:255)
     at com.lk.learn.springmvc.demo.controller.FileUploadController.oneFileUpload(FileUploadController.java:38)
     *
     */
    @RequestMapping("/fileupload")
    public String oneFileUpload(@ModelAttribute FileDomain fileDomain, HttpServletRequest request) {
        /*
         * 文件上传到服务器的位置“/uploadfiles”,该位置是指 workspace\.metadata\.plugins\org.eclipse
         * .wst.server.core\tmp0\wtpwebapps, 发布后使用
         */
        ServletContext servletContext = request.getServletContext();
        String uploadfiles = servletContext.getRealPath("uploadfiles");
        System.out.println("uploadfiles = " + uploadfiles);
//        String realpath = fileUploadLoc;
        String fileName = fileDomain.getMyfile().getOriginalFilename();
        //todo 重命名, 如果不重命名，两次上传的同名文件会覆盖！
        fileName = fileName + "." + UUID.randomUUID();
        File targetFile = new File(uploadfiles, fileName);
        if (!targetFile.getParentFile().exists()) {
            // targetFile.mkdirs(); // 不能这样创建目录
            targetFile.getParentFile().mkdirs();
        }
        // 上传
        try {
            fileDomain.getMyfile().transferTo(targetFile);
            logger.info("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "showFile";
    }

    /*
     * 这种方式也可以获取上传的文件， 更加直接的方式！
     */
    @RequestMapping("/file-upload")
    public ModelAndView upload(@RequestParam(value = "file", required = false) MultipartFile file,
            HttpServletRequest request, HttpSession session) {
        // 文件不为空
        if(!file.isEmpty()) {
            // 文件存放路径
            String path = request.getServletContext().getRealPath("/");
            // 文件名称
            String name = String.valueOf(new Date().getTime()+"_"+file.getOriginalFilename());
            File destFile = new File(path,name);
            // 转存文件
            try {
                file.transferTo(destFile);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
            // 访问的url
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath() + "/" + name;
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("other/home");
        return mv;
    }

    /**
     * 多文件上传
     */
    @RequestMapping("/multifile")
    public String multiFileUpload(@ModelAttribute MultiFileDomain multiFileDomain, HttpServletRequest request) {
        String realpath = request.getServletContext().getRealPath("uploadfiles");
        File targetDir = new File(realpath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        List<MultipartFile> files = multiFileDomain.getMyfile();
        System.out.println("files"+files);
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String fileName = file.getOriginalFilename();
            File targetFile = new File(realpath, fileName);
            // 上传
            try {
                file.transferTo(targetFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "showMulti";
    }

}
