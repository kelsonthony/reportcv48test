package com.example.reportcv48.writer;

import com.example.reportcv48.entity.Customer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CustomerItemWriter implements ItemWriter<Customer> {


    @Override
    public void write(List<? extends Customer> list) throws Exception {
        System.out.println("Thread Name: " + Thread.currentThread().getName());
        System.out.println("Customer list writer" + list);
    }
}
