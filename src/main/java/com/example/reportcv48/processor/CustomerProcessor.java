package com.example.reportcv48.processor;

import com.example.reportcv48.entity.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {

        int age = Integer.parseInt(customer.getAge());
        if (age >= 18) {
            System.out.println("Hello my customer processor" + customer);
            return  customer;
        } else {
            return null;
        }

    }
}
