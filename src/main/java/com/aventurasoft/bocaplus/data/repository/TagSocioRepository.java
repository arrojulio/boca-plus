package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.TagComercio;
import com.aventurasoft.bocaplus.data.entity.TagSocio;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagSocioRepository extends CrudRepository<TagSocio, Integer> {
    @Query("SELECT * FROM tag_socio WHERE socio_id = :socioId ORDER BY UPPER(tag)")
    List<TagSocio> getTagSociosBySocioId(Long socioId);
}
