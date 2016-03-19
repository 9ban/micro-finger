package com.ttsales.microf.love.fans.repository;

import com.ttsales.microf.love.fans.domain.FansInfo;
import com.ttsales.microf.love.fans.domain.FansInfoTag;
import com.ttsales.microf.love.fans.domain.FansTagView;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by liyi on 16/3/6.
 */
@RepositoryRestResource
public interface FansTagViewRepository extends JpaRepository< FansTagView,String>{

    List<FansTagView> findAll(Specification<FansTagView> spec);


}