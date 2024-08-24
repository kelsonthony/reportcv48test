package com.example.reportcv48.config;

import com.example.reportcv48.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        /*if (customer.getCountry().equals("United States")) {
            return  customer;
        } else {
            return null;
        }*/
        int age = Integer.parseInt(customer.getAge());
        if (age >= 18) {
            return  customer;
        } else {
            return null;
        }

        //return customer;
    }
}
