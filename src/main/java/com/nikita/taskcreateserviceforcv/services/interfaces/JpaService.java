package com.nikita.taskcreateserviceforcv.services.interfaces;

public interface JpaService<T, ID> {

    T findById(ID id);

    <S extends T> S save(S entity);

    <S extends T> S update(S entity);

}