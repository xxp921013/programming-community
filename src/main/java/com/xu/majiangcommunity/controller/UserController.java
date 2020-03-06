package com.xu.majiangcommunity.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xu.majiangcommunity.GlobalException;
import com.xu.majiangcommunity.config.MinioConfig;
import com.xu.majiangcommunity.config.MjConfig;
import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.domain.UserExample;
import com.xu.majiangcommunity.dto.BaseResponseBody;
import com.xu.majiangcommunity.dto.FileResponseBody;
import com.xu.majiangcommunity.dto.UserDTO;
import com.xu.majiangcommunity.enums.ExcetionEnmu;
import com.xu.majiangcommunity.service.impl.ArticleService;
import com.xu.majiangcommunity.service.impl.RoundService;
import com.xu.majiangcommunity.service.impl.UserService;

import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@RequestMapping("/localUser")
@Controller
@CrossOrigin
@EnableConfigurationProperties({MjConfig.class, MinioConfig.class})
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoundService roundService;
    @Autowired
    ArticleService articleService;
    @Autowired
    MjConfig mjConfig;
    @Autowired
    MinioConfig minioConfig;

    @PostMapping("/registry")
    public String registryLocalUser(User user, HttpServletRequest req, HttpServletResponse resp, Model model) {
        if (StrUtil.isBlank(user.getAccountId()) || StrUtil.isBlank(user.getName())) {
            log.error("[空的用户名或密码],ip:{}", req.getRemoteAddr());
            throw new GlobalException(ExcetionEnmu.USERNAME_PASSWORD_EMPTY);
        }
        String password = user.getAccountId();
        String username = user.getName();
        password = new Md5Hash(password, username, 3).toString();
        user.setAccountId(password);
        UserExample userExample = new UserExample();
        userExample.or().andNameEqualTo(username);
        List<User> users = userService.selectByExample(userExample);
        if (users != null && users.size() != 0) {
            model.addAttribute("msg", "用户名重复请重新输入");
            return "registry";
        }
        String token = String.valueOf(UUID.randomUUID());
        user.setToken(token);
        int i = userService.insertSelective(user);
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

    @GetMapping("/do_login")
    public String do_login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(User user, HttpSession session) {
        String username = user.getName();
        String password = user.getAccountId();
        password = new Md5Hash(password, username, 3).toString();
        UsernamePasswordToken upToken = new UsernamePasswordToken(username, password);
        //1.获取subject
        Subject subject = SecurityUtils.getSubject();

        //获取session
        String sid = (String) subject.getSession().getId();

        //2.调用subject进行登录
        subject.login(upToken);

        return "loginSuccess";
    }

    @GetMapping("/userDetail")
    public String userDetail(Integer id, Model model) {
        User byId = userService.getById(id);
        String accountId = byId.getAccountId();
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(byId, userDTO);
        int s = roundService.countMyRound(accountId);
        userDTO.setRoundCount(s);
        int y = articleService.countMyArticle(accountId);
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

    @PutMapping("doUploadHead")
    @ResponseBody
    public BaseResponseBody doUploadHead(@RequestParam("name") String name, @RequestParam("url") String url) {
        int i = userService.updateHeadByName(name, url);
        if (i != 1) {
            new BaseResponseBody<>(500, "修改失败");
        }
        return new BaseResponseBody(200, "修改成功");
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse resp, HttpServletRequest req) {
        req.getSession().removeAttribute("user");
        Cookie token = new Cookie("token", "");
        token.setPath("/");
        resp.addCookie(token);
        return "redirect:/";
    }
}
