package com.readora.database;

import java.util.List;

public interface GenericDAO<T, ID> {

    boolean insert(T item);

    boolean update(T item);

    boolean delete(ID id);

    T findById(ID id);

    List<T> findAll();
}