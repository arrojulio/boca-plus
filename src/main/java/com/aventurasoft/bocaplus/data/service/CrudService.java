package com.aventurasoft.bocaplus.data.service;



import com.aventurasoft.bocaplus.data.AbstractEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;
import java.util.Set;

public interface CrudService<T extends AbstractEntity, I> {

	CrudRepository<T, I> getRepository();

	default T save(T entity) {
		return getRepository().save(entity);
	}

	default void delete(T entity)  {
		if (entity == null)
			throw new RuntimeException("invalid entity");
		getRepository().delete(entity);
	}

	default void delete(I id) {
		delete(load(id));
	}

	default long count() {
		return getRepository().count();
	}

	default T load(I id) {
		T entity = getRepository().findById(id).orElse(null);
		if (entity == null)
			throw new RuntimeException("id not found - " + id);

		return entity;
	}

	default Set<T> getAll()
	{
		Set<T> ret = new HashSet<>();
		for (T t : getRepository().findAll())
			ret.add(t);
		return ret;
	}
	T createNew();


}
