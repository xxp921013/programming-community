package com.xu.majiangcommunity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xu.majiangcommunity.dao.ArticleMapper;
import com.xu.majiangcommunity.domain.Article;
import com.xu.majiangcommunity.dto.ArticleDTO;
import com.xu.majiangcommunity.dto.ArticleDetailDTO;
import com.xu.majiangcommunity.dto.GithubTokenDTO;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MajiangCommunityApplicationTests {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StringRedisTemplate srt;
    private static final Long DAYS = 86400000L;

    @Test
    public void contextLoads() {
        List<ArticleDTO> dtObyID = articleMapper.findDTObyID();
        for (ArticleDTO articleDTO : dtObyID) {
            System.out.println(articleDTO);
        }
    }

    @Test
    public void demo1() {
        ArticleDetailDTO articleDetailById = articleMapper.findArticleDetailById("2");
        System.out.println(articleDetailById);
    }

    @Test
    public void demo2() {
        //拿锁
        Long lock = srt.opsForValue().increment("lock");
        try {
            if (lock == 1) {
                ValueOperations<String, String> ops = srt.opsForValue();
                int i = Integer.parseInt(ops.get("stock"));
                ops.set("stock", String.valueOf(i - 1));
                System.out.println("减库存:" + (i - 1));
            } else {
                System.out.println("扣减失败");
            }
        } finally {
            //释放锁
            srt.opsForValue().decrement("lock");
        }
    }

    @Test
    public void demo4() {
        long l = System.currentTimeMillis();
        long t = l - DAYS;
        String s = DateUtil.formatDate(new Date(t));
        System.out.println(s);
        List<Article> lastDayArticle = articleMapper.getLastDayArticle(t);
        System.out.println(lastDayArticle);
    }

    @Test
    public void demo5() {
        String path = ClassUtils.getDefaultClassLoader().getResource("static/images").getPath();
        String substring = path.substring(1, path.length());
        System.out.println(substring);
        String property = System.getProperty("user.home");
        System.out.println(property);
    }
}
