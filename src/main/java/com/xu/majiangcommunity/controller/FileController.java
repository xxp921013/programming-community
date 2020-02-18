package com.xu.majiangcommunity.controller;

import com.xu.majiangcommunity.dto.FileDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
    @RequestMapping("/file/upload")
    public FileDto uploadFile() {
        FileDto success = new FileDto(1, "上传成功", "/images/loading.gif");
        return success;
    }
}
