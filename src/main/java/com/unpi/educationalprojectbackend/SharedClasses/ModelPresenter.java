package com.unpi.educationalprojectbackend.SharedClasses;

import java.util.List;

public interface ModelPresenter<T> {
    public Object present(T object);
    public Object presentMultiple(List<T> objectList);
}
