$(function () {
    onItemClick();
});

function onItemClick() {
    $(".item").click(function () {
        var regionType = $("#provinceType").val();
        var regionCode = $(this).attr("value");
        var regionName=$(this).find("div:first-child").html();
        if (validateMunicipal(regionCode)) {//直辖市
            if (regionType == "self-province") {
                sessionStorage.selfProvinceCode = regionCode;
                sessionStorage.selfArea = regionName;
                sessionStorage.selfLevel = 2;
                history.go(-1);
            } else {
                sessionStorage.compProvinceCode = regionCode;
                sessionStorage.compArea = regionName;
                sessionStorage.compLevel = 2;
                history.go(-1);
            }
            return;
        }
        var type = "comp-province";
        if (regionType == "self-province") {
            type = "self-city";
            sessionStorage.selfProvinceCode = regionCode;
            sessionStorage.selfProvinceName = regionName;
        } else {
            type = "comp-city";
            sessionStorage.compProvinceCode = regionCode;
            sessionStorage.compProvinceName =regionName;
        }
        location.href = "linkPage.do?url=quote/city-select&type=" + type + "&id=" + $(this).attr("value");
    });
    $("#all_province").click(function () {
        var regionType = $("#provinceType").val();
        if (regionType == "self-province") {
            sessionStorage.selfArea = "全国";
            sessionStorage.selfLevel = 1;
        } else {
            sessionStorage.compArea = "全国";
            sessionStorage.compLevel = 1;
        }
        history.go(-1);
    });

}

function validateMunicipal(code) {
    return code == "22" || code == "01" || code == "02" || code == "09";
}