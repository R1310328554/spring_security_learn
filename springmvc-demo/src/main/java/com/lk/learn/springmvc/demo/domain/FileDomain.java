package com.lk.learn.springmvc.demo.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileDomain {
    private String description;
    private MultipartFile myfile;
    /** 省略setter和getter参数*/
}
