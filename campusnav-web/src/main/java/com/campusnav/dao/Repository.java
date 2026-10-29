package com.campusnav.dao;

import java.util.List;

/**
 * Demonstrates OOP: Generic Interface for Data Access Objects.
 * All DAO classes implement this interface to adhere to the abstraction principle.
 *
 * @param <T> Domain entity type
 */
public interface Repository<T> {
    T findById(int id) throws Exception;
    List<T> findAll() throws Exception;
    void save(T entity) throws Exception;
    void deleteById(int id) throws Exception;
}
