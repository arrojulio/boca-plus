package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.TagComercio;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagComercioRepository extends CrudRepository<TagComercio, Integer> {
    @Query("SELECT * FROM tag_comercio WHERE comercio_id = :comercioId ORDER BY UPPER(tag)")
    List<TagComercio> getTagComerciosByComercioId(Integer comercioId);
}
