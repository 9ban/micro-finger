package com.ttsales.microf.love.fans.repository;

import com.ttsales.microf.love.fans.domain.FansInfoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by liyi on 16/3/6.
 */
@RepositoryRestResource
public interface FansTagRepository extends JpaRepository<FansInfoTag,Long>{

    @RestResource(path = "find-fansId")
    List<FansInfoTag> findAllByFansId(@Param("fansId") Long fansId);

    @RestResource(path = "find-tagId")
    List<FansInfoTag> findAllByTagId(@Param("tagId") Long tagId);

    FansInfoTag findByFansIdAndTagId(Long fansId,Long tagId);

}
