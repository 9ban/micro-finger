package com.ttsales.microf.love.common.service;

import com.ttsales.microf.love.common.domain.OrgBrand;
import com.ttsales.microf.love.common.domain.OrgRegion;
import com.ttsales.microf.love.common.domain.OrgStore;
import com.ttsales.microf.love.common.repository.BrandRepository;
import com.ttsales.microf.love.common.repository.RegionRepository;
import com.ttsales.microf.love.common.repository.StoreRepository;
import com.ttsales.microf.love.tag.domain.Tag;
import com.ttsales.microf.love.tag.repository.TagRepository;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lenovo on 2016/3/16.
 */
@Service
public class OrgServiceImpl implements OrgService  {

    @Autowired
    private RegionRepository   regionRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private TagRepository   tagRepository;

   public  List<OrgRegion> findByParentRegionCode(String parentRegionCode){
      return   regionRepository.findByParentRegionCodeOrderByPinyin(parentRegionCode);
   }

    public  List<OrgStore> findByCity(String city){
        return storeRepository.findByCity(city);
    }

    public List<OrgBrand>   getAllBrands(){

        return brandRepository.findAllByOrderByPinyin();
    }

   public  OrgRegion findRegionById(String regionId){
       return regionRepository.findOne(regionId);
   }

    public List<OrgRegion> findByLevel(Integer level){
        return regionRepository.findByLevel(level);
    }

    public List<Tag> findAllBrandTags(){
    return tagRepository.findAllBrandTags();
    }


    public JSONArray getGroupBrands( List<Long> ids){
        List<OrgBrand> brands= brandRepository.findAllByOrderByPinyin();
        String pinyins[]={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        JSONArray typeArrays=new JSONArray();
        for (String py:pinyins){
            JSONArray eachArray=new JSONArray();
            for (OrgBrand b:brands){
                String firstZM=b.getPinyin().substring(0,1).toUpperCase();
                if(py.equals(firstZM)){
                    JSONObject eachObject =getSelectState(b, ids);
                    eachArray.add(eachObject);
                }
            }
            if(eachArray.size()>0){
                JSONObject json=new JSONObject();
                json.put("index",py);
                json.put("data",eachArray);
                typeArrays.add(json);
            }
        }
        return  typeArrays;
    }
    private JSONObject getSelectState(OrgBrand tag,List<Long> ids){
        JSONObject json = new JSONObject();
        json.put("name", tag.getName());
        json.put("id", tag.getId());
        String state = "unselect";
        if (ids==null||ids.size()== 0) {
            json.put("state", state);
                return json;
        }
        for (Long id : ids) {
            if (id ==Long.parseLong(tag.getId())) {//TODO
                state = "select";
                break;
            }
        }
        json.put("state", state);
        return json;
    }
}
