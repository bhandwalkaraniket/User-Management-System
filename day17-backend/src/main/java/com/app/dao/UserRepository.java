package com.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.pojos.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	//Use : Inherited method : List<User> findAll()
	//Use : Inherited method : <S extends T> S save(S entity) -->Saves a given entity.

}
