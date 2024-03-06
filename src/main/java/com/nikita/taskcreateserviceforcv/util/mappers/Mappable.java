package com.nikita.taskcreateserviceforcv.util.mappers;

public interface Mappable<E, D> {

    public E getEntity(D dto);

    public D getDTO(E entity);
}
