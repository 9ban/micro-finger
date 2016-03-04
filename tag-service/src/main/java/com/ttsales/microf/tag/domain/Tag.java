package com.ttsales.microf.tag.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by liyi on 2016/3/4.
 */
@Data
@Entity
@Table(name ="mf_tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

}
