package com.ite5year.services;

import com.ite5year.models.SharedParam;

import java.util.Map;

public interface SharedParametersService {
    SharedParam save(SharedParam sharedParam);
    Map<String, SharedParam> findAll();
    SharedParam findById(String id);
    void update(SharedParam user);
    void delete(String id);
}
