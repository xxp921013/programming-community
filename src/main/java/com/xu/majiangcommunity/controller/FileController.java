package com.xu.majiangcommunity.controller;

import cn.hutool.core.date.DateUtil;
import com.xu.majiangcommunity.GlobalException;
import com.xu.majiangcommunity.config.MinioConfig;
import com.xu.majiangcommunity.config.MjConfig;
import com.xu.majiangcommunity.dto.FileResponseBody;
import com.xu.majiangcommunity.enums.ExcetionEnmu;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@Slf4j
@EnableConfigurationProperties({MjConfig.class, MinioConfig.class})
public class FileController {
    static final String MD_BUCKET = "markdown";
    @Autowired
    MjConfig mjConfig;
    @Autowired
    MinioConfig minioConfig;

    @RequestMapping("/file/upload")
    //RequestParam名称固定不能修改
    public FileResponseBody uploadFile(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file) {
        Boolean tag = isAllowTags(file.getContentType());
        if (!tag) {
            log.error("[错误文件传到后台],文件名:{}", file.getOriginalFilename());
            throw new GlobalException(ExcetionEnmu.FILE_TYPE_ERROR);
        }
        try {
            MinioClient minioClient = new MinioClient(minioConfig.getEndpoint(), minioConfig.getAccessKey(), minioConfig.getSecretKey());
            String originalFilename = file.getOriginalFilename();
            String fileName = DateUtil.now() + originalFilename;
            minioClient.putObject(MD_BUCKET, fileName, file.getInputStream(), file.getContentType());
            log.info("[文件上传成功],{}", file.getOriginalFilename());
            String url = minioClient.getObjectUrl(MD_BUCKET, fileName);
            return new FileResponseBody(200, "文件上传成功", url, 1);
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
            log.error("文件上传错误!");
            throw new GlobalException(ExcetionEnmu.FILE_SERVER_ERROR);
        } catch (InvalidPortException | InvalidBucketNameException | NoSuchAlgorithmException | InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException | InternalException | InvalidArgumentException | InsufficientDataException e) {
            log.error("文件上传错误!");
            e.printStackTrace();
            throw new GlobalException(ExcetionEnmu.FILE_SERVER_ERROR);
        } catch (IOException e) {
            log.error("文件上传错误!");
            e.printStackTrace();
            throw new GlobalException(ExcetionEnmu.FILE_SERVER_ERROR);
        }
    }

    private Boolean isAllowTags(String contentType) {
        List<String> agreeTypes = mjConfig.getAgreeTypes();
        for (String agreeType : agreeTypes) {
            if (agreeType.equals(contentType)) {
                return true;
            }
        }

        return false;
    }
}
