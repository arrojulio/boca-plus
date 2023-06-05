package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.TagComercio;
import com.aventurasoft.bocaplus.data.repository.TagComercioRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagComercioServiceImpl implements TagComercioService {
    private TagComercioRepository tagComercioRepository;
    public TagComercioServiceImpl(TagComercioRepository tagComercioRepository)
    {
        this.tagComercioRepository = tagComercioRepository;
    }
    @Override
    public CrudRepository<TagComercio, Integer> getRepository() {
        return tagComercioRepository;
    }

    @Override
    public TagComercio createNew()
    {
        TagComercio tagComercio = new TagComercio();
        tagComercio.setNew(true);
        return tagComercio;
    }

    @Override
    public List<TagComercio> getTagComerciosByComercioId(Integer comercioId) {
        return tagComercioRepository.getTagComerciosByComercioId(comercioId);
    }
}
