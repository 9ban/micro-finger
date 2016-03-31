/**
 * Created by lenovo on 2016/3/25.
 */
$(function(){
    init();
});

function init(){
    if(sessionStorage.cache){
        sessionData();
    }else{
        ajaxData();
    }
}

function sessionData(){
    var data={};
    var quoteInfo={};
    var priceInfo=JSON.parse( sessionStorage.priceInfo);
    quoteInfo.storeName=sessionStorage.storeName;
    quoteInfo.storeAddr=sessionStorage.storeAddr;
    quoteInfo.regionName=sessionStorage.selfArea;
    quoteInfo.competeNames=sessionStorage.competeNames;
    quoteInfo.competeRegionName=sessionStorage.compArea;
    quoteInfo.compCarTypeInfo=JSON.parse( sessionStorage.compCarTypeInfo);
    data.quoteInfo=quoteInfo;
    data.priceInfo=priceInfo;
  //  var data=JSON.parse(sessionStorage.quoteData);
    showData(data);
}
function ajaxData(){
    $.ajax({
        type : 'POST',
        url : 'initHomeData.do',
        data : {
            'openId':getParamOfUrl("openId")
        },
        dataType : 'json',
        success : function(data, textStatus, jqXHR) {
            if(data.errMsg){
             alert(data.errMsg);
            }
            setSessionData(data);
            showData(data);
        }
    });
}

function setSessionData(data){
    var quoteInfo=data.quoteInfo;
    sessionStorage.queryTimes=data.queryTimes;
    sessionStorage.priceInfo=JSON.stringify(data.priceInfo);
    sessionStorage.storeId=quoteInfo.storeId;
    sessionStorage.storeName=quoteInfo.storeName;
    sessionStorage.storeAddr=quoteInfo.storeAddr;
    sessionStorage.selfArea=quoteInfo.regionName;
    sessionStorage.compArea= quoteInfo.competeRegionName;
    sessionStorage.compCarTypeInfo=JSON.stringify(quoteInfo.compCarTypeInfo);

}

function showData(data){
    var quoteInfo=data.quoteInfo;
    var priceInfo=data.priceInfo;
    $("#storeName").html(quoteInfo.storeName);
    $("#storeAddr").html(quoteInfo.storeAddr);
    $("#cityMaxNum").html(priceInfo.cityMaxNum);
    $("#cityMinNum").html(priceInfo.cityMinNum);
    $("#provinceMaxNum").html(priceInfo.provinceMaxNum);
    $("#provinceMinNum").html(priceInfo.provinceMinNum);
    $("#countryMaxNum").html(priceInfo.countryMaxNum);
    $("#countryMinNum").html(priceInfo.countryMinNum);
    setItemValue($("#selfArea"),quoteInfo.regionName);
    setItemValue($("#compBrand"), getCompeletes().name);
    setItemValue($("#compArea"),quoteInfo.competeRegionName);
}
function setItemValue(_obj,value){
    if(!value){
        _obj.css("color",'#a1a1a1').html("选择");
        return;
    }
    _obj.addClass(".item-show").html(value);
}

function getCompeletes(){
    var compCarTypeInfos=JSON.parse( sessionStorage.compCarTypeInfo);
    var competeNames="";
    var competeIds="";
    for(var i=0;i<compCarTypeInfos.length;i++){
        competeNames+=compCarTypeInfos[i].carTypeNames+",";
        competeIds+=compCarTypeInfos[i].carTypeIds+",";
    }
    return {
        name: competeNames==""?"":competeNames.substr(0,competeNames.length-1),
        id:competeIds==""?"":competeIds.substr(0,competeIds.length-1)
    }
}

function getCompeleteBrandIds(){
    var compCarTypeInfos=null;
    if(sessionStorage.compCarTypeInfo){
        compCarTypeInfos= JSON.parse(sessionStorage.compCarTypeInfo);
    }
    if(compCarTypeInfos==null){
        return "";
    }
    var brandIds="";
    for(var i=0;i<compCarTypeInfos.length;i++){
        brandIds+= compCarTypeInfos[i].brandId+",";
    }
    return brandIds==""?"": brandIds.substr(0,brandIds.length-1);
}


function jumpChoosePg(type){
    sessionStorage.cache=true;
    if(type=="selfArea"){
        location.href="linkPage.do?url=quote/province-select&type=self-province";
    }
    if(type=="compBrand"){
        location.href="linkPage.do?url=quote/brand-select&type=brand&ids="+getCompeleteBrandIds();
    }
    if(type=="compArea"){
        location.href="linkPage.do?url=quote/province-select&type=comp-province";
    }
}

function queryReport(){
    if(!validateParam()){
        return;
    }
    var times= parseInt(sessionStorage.queryTimes);
    if(times>=2){
        humane.info('非订阅用户每天只能查询2次哦！');
        return;
    }
    //TODO 查询结果链接
    console.log( getQueryParam());
    sessionStorage.queryTimes=times+1;

}

function validateParam(){
    var regionName=$("#selfArea").html();
    var compBrand=$("#compBrand").html();
    var compRegionName=$("#compArea").html();
    if(regionName=="选择"){
        humane.info('请选择查询区域！');
        return false;
    }
    if(compBrand=="选择"){
        humane.info('请选择竞品车系！');
        return false;
    }
    if(compRegionName=="选择"){
        humane.info('请选择竞品区域！');
        return false;
    }
    return true;
}

function getQueryParam(){
    var competeRegion="";
    var parentCompRegion="";
    var selfRegion="";
    var parentSelfRegion="";
    if(sessionStorage.compLevel==1){
        competeRegion="00";

    }
    if(sessionStorage.compLevel==2){
        competeRegion=sessionStorage.compProvinceCode;
        parentCompRegion="00";
    }
    if(sessionStorage.compLevel==3){
        parentCompRegion=sessionStorage.compProvinceCode;
        competeRegion=sessionStorage.compCityCode;
    }
    if(sessionStorage.selfLevel==1){
        selfRegion="00";
    }
    if(sessionStorage.selfLevel==2){
        selfRegion=sessionStorage.selfProvinceCode;
        parentSelfRegion="00";
    }
    if(sessionStorage.selfLevel==3){
        selfRegion=sessionStorage.selfCityCode;
        parentSelfRegion=sessionStorage.selfCityCode;
    }
    return {
            openId:getParamOfUrl("openId"),
            storeId:sessionStorage.storeId,
            storeName:sessionStorage.storeName,
            competeRegion:competeRegion,
            competeParentRegion:parentCompRegion,
            competeRegionType:sessionStorage.compLevel,
            competeRegionName: sessionStorage.compArea,
           competeIds:getCompeletes().id,
            competeNames:getCompeletes().name,
            region:selfRegion,
             parentRegion:parentSelfRegion,
            regionType:sessionStorage.selfLevel,
             regionName:sessionStorage.selfArea
    }
}

function getParamOfUrl(param) {
    var url = window.location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {};
    for (var i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[param.toLowerCase()];
    if (typeof(returnValue) == "undefined") {
        return "";
    } else {
        return returnValue;
    }
}