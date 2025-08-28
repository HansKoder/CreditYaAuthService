package org.pragma.creditya.r2dbc.helper;

public interface CustomMapper<E, D> {
    D toData(E entity);
    E toEntity(D data);
}