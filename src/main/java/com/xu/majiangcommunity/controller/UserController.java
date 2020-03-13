package com.xu.majiangcommunity.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xu.majiangcommunity.GlobalException;
import com.xu.majiangcommunity.UserException;
import com.xu.majiangcommunity.config.MinioConfig;
import com.xu.majiangcommunity.config.MjConfig;
import com.xu.majiangcommunity.domain.*;
import com.xu.majiangcommunity.dto.BaseResponseBody;
import com.xu.majiangcommunity.dto.FileResponseBody;
import com.xu.majiangcommunity.dto.PageResult;
import com.xu.majiangcommunity.dto.UserDTO;
import com.xu.majiangcommunity.enums.ExcetionEnmu;
import com.xu.majiangcommunity.interceptor.UserInterceptor;
import com.xu.majiangcommunity.service.ArticleCollectionService;
import com.xu.majiangcommunity.service.SecurityUserService;
import com.xu.majiangcommunity.service.impl.ArticleCollectionServiceImpl;
import com.xu.majiangcommunity.service.impl.ArticleService;
import com.xu.majiangcommunity.service.impl.RoundService;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequestMapping("/localUser")
@Controller
@CrossOrigin
@EnableConfigurationProperties({MjConfig.class, MinioConfig.class})
@Slf4j
public class UserController {
    @Autowired
    private ArticleCollectionServiceImpl articleCollectionService;
    @Autowired
    RoundService roundService;
    @Autowired
    ArticleService articleService;
    @Autowired
    MjConfig mjConfig;
    @Autowired
    MinioConfig minioConfig;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    private SecurityUserService securityUserService;

    @PostMapping("/registry")
    public String registryLocalUser(SecurityUser user, HttpServletRequest req, HttpServletResponse resp, Model model) {
        if (StrUtil.isBlank(user.getPassword()) || StrUtil.isBlank(user.getUsername())) {
            log.error("[空的用户名或密码],ip:{}", req.getRemoteAddr());
            throw new GlobalException(ExcetionEnmu.USERNAME_PASSWORD_EMPTY);
        }
        String password = user.getPassword();
        String username = user.getUsername();
        password = encoder.encode(password);
        user.setPassword(password);
        SecurityUserExample example = new SecurityUserExample();
        example.or().andUsernameEqualTo(username);
        List<SecurityUser> users = securityUserService.selectByExample(example);
        if (users != null && users.size() != 0) {
            model.addAttribute("msg", "用户名重复请重新输入");
            return "registry";
        }
        int i = securityUserService.insertSelective(user);
        if (i != 1) {
            model.addAttribute("msg", "注册失败,请重新注册");
            return "registry";
        }
        return "registrySuccess";
    }

    @GetMapping("/do_register")
    public String do_register() {
        return "registry";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "loginSuccess";
    }

    @GetMapping("/userDetail")
    public String userDetail(Model model) {
        SecurityUser user = UserInterceptor.getUser();

        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(user, userDTO);
        int s = roundService.countMyRound(user.getUsername());
        userDTO.setRoundCount(s);
        int y = articleService.countMyArticle(user.getUsername());
        userDTO.setArticleCount(y);
        model.addAttribute("userDetail", userDTO);

        return "userDetail";
    }

    @PostMapping("/uploadImg")
    @ResponseBody
    public FileResponseBody uploadImg(MultipartFile file) throws IOException {
        Boolean tag = isAllowTags(file.getContentType());
        if (!tag) {
            log.error("[错误文件传到后台],文件名:{}", file.getOriginalFilename());
            throw new GlobalException(ExcetionEnmu.FILE_TYPE_ERROR);
        }
        try {
            MinioClient minioClient = new MinioClient(minioConfig.getEndpoint(), minioConfig.getAccessKey(), minioConfig.getSecretKey());
            String originalFilename = file.getOriginalFilename();
            String fileName = DateUtil.now() + originalFilename;
            minioClient.putObject(minioConfig.getBucketName(), fileName, file.getInputStream(), file.getContentType());
            log.info("[文件上传成功],{}", file.getOriginalFilename());

            String url = minioClient.getObjectUrl(minioConfig.getBucketName(), fileName);

            return new FileResponseBody(200, "文件上传成功", url);
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
            log.error("文件上传错误!");
            throw new GlobalException(ExcetionEnmu.FILE_SERVER_ERROR);
        } catch (InvalidPortException | InvalidBucketNameException | NoSuchAlgorithmException | InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException | InternalException | InvalidArgumentException | InsufficientDataException e) {
            log.error("文件上传错误!");
            e.printStackTrace();
            throw new GlobalException(ExcetionEnmu.FILE_SERVER_ERROR);
        }
//        String path = ClassUtils.getDefaultClassLoader().getResource("static/images").getPath();
//        String substring = path.substring(1, path.length());
//        String ext = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
//        File file1 = new File(substring + "/" + file.getOriginalFilename());
//        String fileName = StringUtils.substringBeforeLast(file.getOriginalFilename(), ".");
//        if (file1.exists()) {
//            file1 = new File(substring + "/" + fileName + DateUtil.current(false) + "." + ext);
//        }
//        FileOutputStream fileOutputStream = new FileOutputStream(file1);
//        FileInputStream inputStream = (FileInputStream) file.getInputStream();
//        FileChannel channelIn = null;
//        FileChannel channelOut = null;
//        try {
//            channelIn = inputStream.getChannel();
//            channelOut = fileOutputStream.getChannel();
//            channelIn.transferTo(0, channelIn.size(), channelOut);
//            String url = "http://localhost:8080/images/" + file.getOriginalFilename();
//            FileDto fileDto = new FileDto();
//            fileDto.setUrl(url);
//            fileDto.setSuccess(1);
//            fileDto.setMessage("上传成功");
//            return fileDto;
//        } finally {
//            channelIn.close();
//            channelOut.close();
//        }
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

    @PutMapping("/doUploadHead")
    @ResponseBody
    public BaseResponseBody doUploadHead(@RequestParam("url") String url) {
        SecurityUser user = UserInterceptor.getUser();

        int i = securityUserService.updateHeadByName(user.getUsername(), url);
        if (i != 1) {
            return new BaseResponseBody<>(500, "修改失败");
        }
        return new BaseResponseBody(200, "修改成功");
    }

//    @GetMapping("/logout")
//    public String logout(HttpServletResponse resp, HttpServletRequest req) {
//        req.getSession().removeAttribute("user");
//        Cookie token = new Cookie("token", "");
//        token.setPath("/");
//        resp.addCookie(token);
//        return "redirect:/";
//    }

    @GetMapping("/myCollection")
    public String getMyCollection(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page) {
        SecurityUser user = UserInterceptor.getUser();

        List<Integer> articleIds = articleCollectionService.getUserCollectionArticleIds(user.getUsername());
        PageResult<List<ArticleEs>> byIds = null;
        if (CollectionUtil.isNotEmpty(articleIds)) {
            byIds = articleService.findByIds(articleIds, page);
        } else {
            byIds = new PageResult<List<ArticleEs>>();
            byIds.setMessage("该用户无收藏");
            byIds.setCode(200);
            byIds.setPageNum(1);
            byIds.setTotal(0);
        }
        model.addAttribute("articles", byIds);
        return "myCollection";
    }

    @PutMapping("/addCollection")
    @ResponseBody
    public BaseResponseBody addCollection(@RequestParam("articleId") Integer articleId) {
        SecurityUser user = UserInterceptor.getUser();
        ArticleCollection articleCollection = new ArticleCollection();
        articleCollection.setArticleId(articleId);
        articleCollection.setUsername(user.getUsername());
        int i = articleCollectionService.insertSelective(articleCollection);
        BaseResponseBody responseBody = null;
        if (i != 1) {
            responseBody = new BaseResponseBody(500, "添加失败");

        } else {
            responseBody = new BaseResponseBody(200, "添加成功");
        }
        return responseBody;
    }

    @DeleteMapping("/removeCollection")
    @ResponseBody
    public BaseResponseBody removeCollection(@RequestParam("articleId") Integer articleId) {
        SecurityUser user = UserInterceptor.getUser();
        ArticleCollectionExample articleCollectionExample = new ArticleCollectionExample();
        articleCollectionExample.or().andArticleIdEqualTo(articleId).andUsernameEqualTo(user.getUsername());
        articleCollectionService.deleteByExample(articleCollectionExample, user.getUsername());
        return new BaseResponseBody(200, "删除成功");
    }
}
