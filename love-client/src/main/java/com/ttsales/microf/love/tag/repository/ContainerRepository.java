package com.ttsales.microf.love.tag.repository;

import com.ttsales.microf.love.tag.domain.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liyi on 2016/3/4.
 */

@RepositoryRestResource
@Repository
public interface ContainerRepository extends JpaRepository<Container,Long>{

    @RestResource(path = "find-qrcodeTicket")
    Container findByQrcodeTicket(@Param("qrcodeTicket") String qrcodeTicket);

    @RestResource(path = "find-name")
    Container findByName(@Param("name") String name);

    @RestResource(path = "find-name-like")
    List<Container> findByNameContaining(String name);

    @Query("select c from Container c join TagContainer t on c.id=t.containerId where t.tagId=:tagId")
    List<Container> findAllByTagId(Long tagId);
}