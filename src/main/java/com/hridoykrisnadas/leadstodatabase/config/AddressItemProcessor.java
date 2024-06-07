package com.hridoykrisnadas.leadstodatabase.config;

import com.hridoykrisnadas.leadstodatabase.model.Address;
import com.hridoykrisnadas.leadstodatabase.model.Person;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class AddressItemProcessor implements ItemProcessor<Address, Address> {

    @Override
    public Address process(Address item) throws Exception {
        return item;
    }
}
