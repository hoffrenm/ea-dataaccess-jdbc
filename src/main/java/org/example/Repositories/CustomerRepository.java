package org.example.Repositories;

import org.example.Models.Customer;
import java.util.List;
public interface CustomerRepository extends CRUDrepository<Customer, Integer> {
    List<Customer> findAll();
    Customer findById(Integer id);
    int insert(Customer object);
    int update(Customer object);
    int delete(Customer object);
    int deleteById(Integer id);
}