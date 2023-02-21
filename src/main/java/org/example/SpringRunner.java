package org.example;

import org.example.Models.Customer;
import org.example.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringRunner implements ApplicationRunner {

    @Autowired
    CustomerRepository customerRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringRunner.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Get customer by id
        System.out.println(customerRepository.findById(1));

        // Get page of customers by limit and page number
        System.out.println(customerRepository.customerPage(8, 2));

        // Get existing customer and update information
        Customer customer = customerRepository.findById(1);
        customer.setFirstName("Levis");
        System.out.println(customerRepository.update(customer));

        // Get customer who is the highest spender
        System.out.println(customerRepository.highestSpender());
    }
}
