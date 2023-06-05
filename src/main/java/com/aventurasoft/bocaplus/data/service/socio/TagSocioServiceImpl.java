package com.aventurasoft.bocaplus.data.service.socio;

import com.aventurasoft.bocaplus.data.entity.TagSocio;
import com.aventurasoft.bocaplus.data.repository.TagSocioRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagSocioServiceImpl implements TagSocioService {
    private TagSocioRepository tagSocioRepository;
    public TagSocioServiceImpl(TagSocioRepository tagSocioRepository)
    {
        this.tagSocioRepository = tagSocioRepository;

    }
    @Override
    public CrudRepository<TagSocio, Integer> getRepository() {
        return tagSocioRepository;
    }

    @Override
    public TagSocio createNew()
    {
        TagSocio tagSocio = new TagSocio()  ;
        tagSocio.setNew(true);
        return tagSocio;
    }

    @Override
    public List<TagSocio> getTagSociosBySocioId(Long socioId) {
        return tagSocioRepository.getTagSociosBySocioId(socioId);
    }
}
