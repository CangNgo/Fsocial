package com.fsocial.postservice.services;

import java.util.List;

public interface RedisService {
    void saveData(String key, String value);
    String getData(String key);
    void saveList(String key, String value);
    List<String> getList(String key);
}
