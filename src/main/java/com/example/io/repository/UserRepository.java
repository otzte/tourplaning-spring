package com.example.io.repository;

import com.example.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository //erzeugt bean von Type UserRepository
public interface UserRepository extends CrudRepository<UserEntity,Long> {
    //standardisierte Benennung von Query Methoden
    //find
    //By  muss mit dabei sein
    //Email
    UserEntity findByEmail(String email);
}
