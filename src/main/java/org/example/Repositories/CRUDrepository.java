package org.example.Repositories;

import java.util.List;

/**
 * Interface Generic CRUD repository of repository pattern
 * @param <T> the type parameter for Customer
 * @param <U> the type parameter for Integer
 */

public interface CRUDrepository<T, U> {

    List<T> findAll();

    T findById(U id);
    int insert(T object);
    int update(T object);
    int delete(T object);
    int deleteById(U id);
}