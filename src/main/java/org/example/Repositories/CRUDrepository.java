package org.example.Repositories;

import java.util.List;

public interface CRUDrepository<T, U> {

    List<T> findAll();
    T findById(U id);
    int insert(T object);
    int update(T object);
    int delete(T object);
    int deleteById(U id);
}