package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("select * from \"users\" u where UPPER(u.email) = UPPER(:email)")
	User 	findByEmailIgnoreCase(String email);

	@Query("select * from \"users\" ")
	Page<User> findBy(Pageable pageable);

	@Query("select * from \"users\"  u where UPPER(u.email) LIKE CONCAT('%' , UPPER(:emailLike) , '%')  " +
			"OR UPPER(u.first_name) LIKE CONCAT('%' ,  UPPER(:firstNameLike) , '%') " +
			"OR UPPER(u.last_name) LIKE CONCAT('%' , UPPER(:lastNameLike) , '%') " +
			"OR UPPER(u.role) LIKE CONCAT('%' , UPPER(:roleLike) , '%') "
	)
	Page<User> findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
			String emailLike, String firstNameLike, String lastNameLike, String roleLike, Pageable pageable);

	@Query("select count(*) from \"users\"  u where UPPER(u.email) LIKE CONCAT('%' , UPPER(:emailLike) , '%') " +
			"OR UPPER(u.first_name) LIKE CONCAT('%' ,  UPPER(:firstNameLike) , '%') " +
			"OR UPPER(u.last_name) LIKE CONCAT('%' , UPPER(:lastNameLike) , '%') " +
			"OR UPPER(u.role) LIKE CONCAT('%' , UPPER(:roleLike) , '%')")
	long countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
			String emailLike, String firstNameLike, String lastNameLike, String roleLike);


	@Query("select * from \"users\"  u where UPPER(u.first_name) LIKE CONCAT('%' +, UPPER(:firstNameLike) , '%') " +
			"OR UPPER(u.last_name) LIKE CONCAT('%' , UPPER(:lastNameLike) , '%') "
	)
	Page<User> findByFirstNameLastNameLikeIgnoreCase(String firstNameLike, String lastName, Pageable pageable);

	@Query("select count(*) from \"users\"  u where UPPER(u.first_name) LIKE CONCAT('%' ,  UPPER(:firstNameLike) , '%') " +
			"OR UPPER(u.last_name) LIKE CONCAT('%' , UPPER(:lastNameLike) , '%') "
	)
	long countByFirstNameLastNameLikeIgnoreCase(String firstNameLike, String lastName);

	@Query("select * from \"users\"  u where UPPER(u.first_name) LIKE CONCAT('%' ,  UPPER(:firstNameLike) , '%') " +
			"OR UPPER(u.last_name) LIKE CONCAT('%' , UPPER(:lastNameLike) , '%') " +
			"OR UPPER(u.email) LIKE CONCAT('%' , UPPER(:emailLike) , '%') "
	)
	Set<User> findByFirstNameLastNameEmailLikeIgnoreCase(String firstNameLike, String lastNameLike, String emailLike);


}
