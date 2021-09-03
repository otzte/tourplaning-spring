package com.example.io.repository;

import com.example.io.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository //erzeugt bean von Type UserRepository
public interface UserRepository extends PagingAndSortingRepository<UserEntity,Long> {
    //pagingandsortingrepository enables us to call paging methods of spring framework
    //standardisierte Benennung von Query Methoden
    //find
    //By  muss mit dabei sein
    //Email
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String id);//composes sql query
}
