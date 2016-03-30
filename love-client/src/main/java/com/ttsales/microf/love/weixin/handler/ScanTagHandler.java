package com.ttsales.microf.love.weixin.handler;


import com.ttsales.microf.love.article.domain.Article;
import com.ttsales.microf.love.article.domain.ArticleTag;
import com.ttsales.microf.love.article.service.ArticleService;
import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.fans.service.FansService;
import com.ttsales.microf.love.qrcode.domain.QrCode;
import com.ttsales.microf.love.qrcode.service.QrcodeService;
import com.ttsales.microf.love.quote.domain.QueryInfo;
import com.ttsales.microf.love.quote.service.QuoteService;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.domain.TagContainer;
import com.ttsales.microf.love.tag.service.TagService;
import com.ttsales.microf.love.util.WXApiException;
import com.ttsales.microf.love.weixin.message.PubResponseMessage;
import com.ttsales.microf.love.weixin.message.TextMsgContent;
import com.ttsales.microf.love.weixin.web.support.WXCallbackContext;
import com.ttsales.microf.love.weixin.web.support.WXCallbackHandler;
import org.apache.catalina.util.URLEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liyi on 2016/3/10.
 * 扫描文章中的兴趣点二维码
 */
@Component
public class ScanTagHandler implements WXCallbackHandler {

    @Autowired
    private QrcodeService qrcodeService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private FansService fansService;

    @Autowired
    private QuoteService quoteService;

    Logger logger = Logger.getLogger(ScanTagHandler.class);

    @Override
    public void handle(WXCallbackContext context) {
        String eventKey = context.readChildValue(context.readRoot(), "EventKey");
        String ticket = context.readChildValue(context.readRoot(), "Ticket");
        ticket = URLEncoder.DEFAULT.encode(ticket);
        String openId = context.readFromUserName();
        List<Long> tags = null;
        if (eventKey.startsWith("qrscene_")) {
            eventKey = eventKey.substring("qrscene_".length());
        }
        String contextStr = null;
        QrCode qrcode = qrcodeService.getQrcode(eventKey);
        if (qrcode != null) {
            Article article = articleService.getArticleByTicket(ticket);
            if (QrCode.REF_TYPE_ARTICLE.equals(qrcode.getRefType())) {
                tags = articleService.getArticleTags(article.getId())
                        .stream().map(ArticleTag::getTagId).collect(Collectors.toList());
                contextStr = getContextStr(article);
            } else if (QrCode.REF_TYPE_TAG_CONTAINER.equals(qrcode.getRefType())) {
                tags = tagService.getTagContainer(ticket).stream().map(TagContainer::getTagId)
                        .collect(Collectors.toList());
            } else if (QrCode.REF_TYPE_QUOTE.equals(qrcode.getRefType())) {
                QueryInfo queryInfo = quoteService.queryQueryInfo(openId);
                List<Tag> tagList = quoteService.getQuoteTag(openId);
                fansService.updateOrgStore(openId, queryInfo.getStoreId());
                tags = tagList.stream().map(Tag::getId).collect(Collectors.toList());
                contextStr = getContextStr(queryInfo);
            }
            fansService.createFansTag(openId, tags);

            try {
               sendResponseMessage(context,contextStr);
            } catch (Exception e) {
                logger.error(e);
            }

        }
    }

    private String getContextStr(Article article){
        String contextStr;
        if (!StringUtils.isEmpty(article.getUrl())) {
            contextStr = "<a href=\"" + article.getUrl() + "\"> " + article.getTip() + "</a>";
        } else {
            contextStr = article.getTip();
        }
        return contextStr;
    }

    private String getContextStr(QueryInfo queryInfo){
        return  "您已成功订阅“" + queryInfo.getRegionName() + "地区4S店报价情报“，我们将定期为您推送最新内容！更多精彩发现，请直接点击主菜单“我的”——“焦点订阅”！";
    }

    private void sendResponseMessage(WXCallbackContext context,String contextStr) throws WXApiException,IOException{
        if (contextStr == null||"".equals(contextStr)) {
            return;
        }
        PubResponseMessage message = new PubResponseMessage();
        message.setFromUserName(context.readToUserName());
        message.setToUserName(context.readFromUserName());
        message.setCreateTime(System.currentTimeMillis() / 1000);
        TextMsgContent textMsgContent = new TextMsgContent();
        textMsgContent.setContent(contextStr);
        message.setContent(textMsgContent);
        context.writeXML(message.toMsgString());
    }


    @Override
    public boolean accept(WXCallbackContext context) {
        if (!"event".equals(context.readMsgType())) {
            return false;
        }
        String eventType = context.readChildValue(context.readRoot(), "Event");
        if ("SCAN".equals(eventType)) {
            return true;
        }
        if ("subscribe".equals(eventType)) {
            String eventKey = context.readChildValue(context.readRoot(), "EventKey");
            return eventKey != null && eventKey.startsWith("qrscene_");
        }
        return false;
    }
}
