package com.xu.majiangcommunity.provider;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xu.majiangcommunity.dto.GithubTokenDTO;
import com.xu.majiangcommunity.dto.GithubUserDTO;
import okhttp3.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class GithubPro {
    public String getAccessToken(GithubTokenDTO accessTokenDTO) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, JSONUtil.toJsonStr(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String s = StrUtil.subBefore(string, "&", false);
            String s1 = StrUtil.subAfter(s, "=", true);
            System.out.println(s1);
            return string;
        }
    }

    public GithubUserDTO getUser(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?" + accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            GithubUserDTO githubUserDTO = JSONUtil.toBean(response.body().string(), GithubUserDTO.class);
            return githubUserDTO;
        }
    }
}

