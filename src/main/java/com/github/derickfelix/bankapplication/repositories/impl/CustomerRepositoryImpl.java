/*
 * The MIT License
 *
 * Copyright 2019 Derick Felix.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.derickfelix.bankapplication.repositories.impl;

import com.github.derickfelix.bankapplication.database.BankAppTemplate;
import com.github.derickfelix.bankapplication.database.RowMapper;
import com.github.derickfelix.bankapplication.models.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.github.derickfelix.bankapplication.repositories.CustomerRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerRepositoryImpl implements CustomerRepository {

    private final BankAppTemplate template;
    
    public CustomerRepositoryImpl()
    {
        this.template = new BankAppTemplate();
    }

    @Override
    public List<Customer> findAll()
    {
        String sql = "select * from customers";
        
        return template.queryForList(sql, null, new CustomerMapper());
    }

    @Override
    public Optional<Customer> find(Long id)
    {
        String sql = "select * from customers where id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        
        return template.queryForObject(sql, params, new CustomerMapper());
    }

    @Override
    public void save(Customer model)
    {
        String sql;
        Map<String, Object> params = new HashMap<>();
        
        if (model.getId() != null) {
            sql = "insert into customers (name, address, sex, account_number, account_type) "
                    + "values (:name, :address, :sex, :account_number, :account_type)";
        } else {
            sql = "update customers set name = :name, address = :address, "
                    + "sex = :sex, account_number = :account_number, account_type = :account_type "
                    + "where id = :id";

            params.put("id", model.getId());            
        }
        
        params.put("name", model.getName());
        params.put("address", model.getAddress());
        params.put("sex", model.getSex());
        params.put("account_number", model.getAccountNumber());
        params.put("account_type", model.getAccountType());
        
        template.update(sql, params);
    }

    @Override
    public Optional<Customer> deleteById(Long id)
    {
        Optional<Customer> optional = find(id);
        
        optional.ifPresent(customer -> {
            String sql = "delete from customers where id = :id";
            Map<String, Object> params = new HashMap<>();
            params.put("id", customer.getId());

            template.update(sql, params);
        });
        
        return optional;
    }
    
    public class CustomerMapper implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs) throws SQLException
        {
            Customer customer = new Customer();
            customer.setId(rs.getLong("id"));
            customer.setName(rs.getString("name"));
            customer.setAddress(rs.getString("address"));
            customer.setSex((rs.getString("sex").charAt(0)));
            customer.setAccountNumber(rs.getString("account_number"));
            customer.setAccountType(rs.getString("account_type"));

            return customer;
        }
        
    }
}