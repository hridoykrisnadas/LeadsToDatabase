package com.hridoykrisnadas.leadstodatabase.config;

import com.hridoykrisnadas.leadstodatabase.model.Address;
import com.hridoykrisnadas.leadstodatabase.model.Person;
import com.hridoykrisnadas.leadstodatabase.repository.AddressRepository;
import com.hridoykrisnadas.leadstodatabase.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressWriter implements ItemWriter<Address> {
    private final AddressRepository addressRepository;

    @Override
    public void write(Chunk<? extends Address> chunk) throws Exception {
        addressRepository.saveAll(chunk);
    }
}
