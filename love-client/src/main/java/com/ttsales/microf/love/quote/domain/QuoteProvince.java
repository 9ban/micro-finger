package com.ttsales.microf.love.quote.domain;

import com.ttsales.microf.love.domainUtil.SuperEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by liyi on 2016/3/24.
 */
@Data
@Entity
@Table(name = "dat_price_province")
public class QuoteProvince {

    private String province;

    @Column
    @Id
    private String id;

    @Column(name="store_fullname")
    private String storeFullname;

    @Column(name="store_id")
    private String storeId;

    @Column(name="store_name")
    private String storeName;

    @Column(name="min_model_num")
    private String minModelNum;

    @Column(name="max_model_num")
    private String maxModelNum;

}