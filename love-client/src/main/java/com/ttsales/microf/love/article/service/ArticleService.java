package com.ttsales.microf.love.article.service;

import com.ttsales.microf.love.article.domain.Article;
import com.ttsales.microf.love.article.domain.ArticleTag;
import com.ttsales.microf.love.util.WXApiException;
import org.apache.http.HttpException;

import java.util.List;

/**
 * Created by liyi on 2016/3/11.
 */
public interface ArticleService {

    void sychnoizeArticle() throws WXApiException, HttpException;

    void sendArticleByTags(String mediaId, List<Long> tags);

    void sendArticle(String mediaId, List<String> openIds);

    List<ArticleTag> getArticleTags(Long articleId);

    String createQrcodeTicket(Long articleId) throws WXApiException, HttpException;

    Article getArticleByTicket(String qrcodeTicket);
}