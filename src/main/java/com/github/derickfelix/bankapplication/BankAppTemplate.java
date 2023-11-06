package com.github.derickfelix.bankapplication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.derickfelix.bankapplication.database.RowMapper;

public interface BankAppTemplate {

    void update(String sql, Map<String, Object> params);

    <T> Optional<T> queryForObject(String sql, Map<String, Object> params, RowMapper<T> rowMapper);

    <T> List<T> queryForList(String sql, Map<String, Object> params, RowMapper<T> rowMapper);

}