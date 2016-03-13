package com.ttsales.microf.love.client.weixin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liyi on 2016/3/10.
 */
@Configuration
@RefreshScope
public class MPApiConfig {

    @Value("${wx.apiUrl}")
    private String apiUrl;

    @Value("${wx.token}")
    private String token;

    @Value("${app.url}")
    private String appUrl;

    public static  final String QRCODE_SHOW_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";

    public String getSendCustomerMessageApi(){
        return apiUrl+"/api/message-custom-send";
    }

    public String getMaterialApi(){
        return apiUrl+"/api/material-batchget-material";
    }

    public String getCreateQrCodeTicketApi(){
        return apiUrl+"/api/qrcode-create";
    }

    public String getToken(){
        return  token;
    }

    public String getOauthCodeApi(){
        return apiUrl+"/code/connect-oauth2-authorize";
    }

    public String getOathAccessToken(){ return apiUrl+"/auth/sns-oauth2-access_token";}

    public String getAppUrl() {
        return appUrl;
    }
}