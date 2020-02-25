package com.xu.majiangcommunity.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xu.majiangcommunity.GlobalException;
import com.xu.majiangcommunity.config.MjConfig;
import com.xu.majiangcommunity.domain.User;
import com.xu.majiangcommunity.domain.UserExample;
import com.xu.majiangcommunity.dto.BaseResponseBody;
import com.xu.majiangcommunity.dto.FileDto;
import com.xu.majiangcommunity.dto.UserDTO;
import com.xu.majiangcommunity.enums.ExcetionEnmu;
import com.xu.majiangcommunity.service.ArticleService;
import com.xu.majiangcommunity.service.RoundService;
import com.xu.majiangcommunity.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.UUID;

@RequestMapping("/localUser")
@Controller
@CrossOrigin
@EnableConfigurationProperties(MjConfig.class)
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoundService roundService;
    @Autowired
    ArticleService articleService;
    @Autowired
    MjConfig mjConfig;

    @PostMapping("/registry")
    public String registryLocalUser(User user, HttpServletResponse resp, Model model) {
        if (StrUtil.isBlank(user.getAccountId()) || StrUtil.isBlank(user.getName())) {
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
    public String login(User user) {
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

    @GetMapping("userDetail")
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
    public FileDto uploadImg(MultipartFile file) throws IOException {
        Boolean tag = isAllowTags(file.getContentType());
        if (!tag) {
            throw new GlobalException(ExcetionEnmu.FILE_TYPE_ERROR);
        }
        String path = ClassUtils.getDefaultClassLoader().getResource("static/images").getPath();
        String substring = path.substring(1, path.length());
        String ext = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        File file1 = new File(substring + "/" + file.getOriginalFilename());
        String fileName = StringUtils.substringBeforeLast(file.getOriginalFilename(), ".");
        if (file1.exists()) {
            file1 = new File(substring + "/" + fileName + DateUtil.current(false) + "." + ext);
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file1);
        FileInputStream inputStream = (FileInputStream) file.getInputStream();
        FileChannel channelIn = null;
        FileChannel channelOut = null;
        try {
            channelIn = inputStream.getChannel();
            channelOut = fileOutputStream.getChannel();
            channelIn.transferTo(0, channelIn.size(), channelOut);
            String url = "http://localhost:8080/images/" + file.getOriginalFilename();
            FileDto fileDto = new FileDto();
            fileDto.setUrl(url);
            fileDto.setSuccess(1);
            fileDto.setMessage("上传成功");
            return fileDto;
        } finally {
            channelIn.close();
            channelOut.close();
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

    @PutMapping("doUploadHead")
    @ResponseBody
    public BaseResponseBody doUploadHead(@RequestParam("name") String name, @RequestParam("url") String url) {
        int i = userService.updateHeadByName(name, url);
        if (i != 1) {
            new BaseResponseBody<>(500, "修改失败");
        }
        return new BaseResponseBody(200, "修改成功");
    }
}
