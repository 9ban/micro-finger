package com.ttsales.microf.love.quote.web;

import com.ttsales.microf.love.article.service.ArticleService;
import com.ttsales.microf.love.common.domain.OrgBrand;
import com.ttsales.microf.love.common.domain.OrgCarType;
import com.ttsales.microf.love.common.domain.OrgRegion;
import com.ttsales.microf.love.common.domain.OrgStore;
import com.ttsales.microf.love.common.repository.CarTypeRepository;
import com.ttsales.microf.love.common.service.OrgService;
import com.ttsales.microf.love.common.service.OrgServiceImpl;
import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.fans.service.FansService;
import com.ttsales.microf.love.quote.domain.QueryInfo;
import com.ttsales.microf.love.quote.service.QuoteService;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.service.TagService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * List
 * Created by zwt on 2016/3/15.
 */
@Controller
@RequestMapping("quoteHome")
public class QuoteHomeController {

    @Autowired
    private OrgService  orgService;

    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value = "/init")
    public String initQuoteHome() {
        return "quote/quoteHome";
    }

    @RequestMapping(value = "/initHomeData" , method = RequestMethod.POST)
    @ResponseBody
    public JSONObject initHomeData(String openId) {
        JSONObject json=new JSONObject();
        json.put("quoteInfo",getQueryInfo( openId));
        json.put("priceInfo",quoteService.queryStorePrice(getStoreByOpenId(openId).getStoreId()));
        json.put("queryTimes",quoteService.countQueryTims(openId));
        return json;
    }

    private OrgStore getStoreByOpenId(String openId){
        //TODO 修改
        OrgStore orgStore=new OrgStore();
        orgStore.setStoreId("0012513001");
        orgStore.setStoreName("大理惠恒丰赢店");
        orgStore.setAddress("杭州市西湖区文三路34号");
        return orgStore;
    }

    private JSONObject getQueryInfo(String openId){
        OrgStore orgStore= getStoreByOpenId(openId);


        String competeIds="01670589da9b11e5a35700155d000e3c,07deda30da9f11e5a35700155d000e3c";
        JSONObject json=new JSONObject();
        json.put("region","11");
        json.put("regionName","杭州市");
        json.put("competeRegion","11");
        json.put("competeRegionName","杭州市");
        json.put("competeIds",competeIds);
        json.put("competeNames","高尔夫A7,帕杰罗");
        json.put("competeRegion","11");
        json.put("competeRegionName","杭州市");
        json.put("storeName",orgStore.getStoreName());
        json.put("storeId",orgStore.getStoreId());
        json.put("storeAddr",orgStore.getAddress());
        json.put("compCarTypeInfo",orgService.covertJson(competeIds));
        return json;
    }

    @RequestMapping(value = "/linkPage")
    public String linkPage(HttpServletRequest request,String type,String url,String ids,String id) {
        if("self-province".equals(type)||"comp-province".equals(type)){
            request.setAttribute("regions",getRegions("00"));
            request.setAttribute("type",type);
        }
        if("self-city".equals(type)||"comp-city".equals(type)){
            request.setAttribute("regions",getRegions(id));
            request.setAttribute("type",type);
        }
        if("brand".equals(type)){
            request.setAttribute("brands",getGroupBrands(ids));
        }
        if("carType".equals(type)){
            List<OrgCarType> carTypes= orgService.queryCarTypesByBrandId(id);
            OrgBrand brand=orgService.getBrandById(id);
            request.setAttribute("brand",brand);
            request.setAttribute("carTypes",getCarTypes(carTypes,ids));
        }
        return url;
    }

    private JSONArray getGroupBrands(String brandIds){
        return  orgService.getGroupBrands(brandIds, OrgServiceImpl.BRAND_TYPE_PRICE);
    }

    private JSONArray getRegions(String code){
        JSONArray array=new JSONArray();
        List<OrgRegion> regions= orgService.findByParentRegionCode(code);
        return  JSONArray.fromObject(regions);
    }



    private JSONArray getCarTypes(List<OrgCarType> tags, String tagIds) {
        JSONArray array = new JSONArray();
        for (OrgCarType tag : tags) {
            JSONObject json = new JSONObject();
            json.put("name", tag.getName());
            json.put("id", tag.getId());
            String state = "unselect";
            if (tagIds == null) {
                json.put("state", state);
                array.add(json);
                continue;
            }
            String tagId[]=tagIds.split(",");
            for (String id : tagId) {
                if (id.equals(tag.getId())) {
                    state = "select";
                    break;
                }
            }
            json.put("state", state);
            array.add(json);
        }
        return array;

    }
}