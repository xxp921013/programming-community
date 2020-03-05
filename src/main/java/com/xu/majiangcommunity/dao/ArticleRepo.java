package com.xu.majiangcommunity.dao;

import com.xu.majiangcommunity.domain.ArticleEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleRepo extends ElasticsearchRepository<ArticleEs, Integer> {
    Page<ArticleEs> findAllByDescriptionLikeOrTittleLike(String keyWord, String keyWord2, Pageable pageable);
}
