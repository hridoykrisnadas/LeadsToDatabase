package com.hridoykrisnadas.leadstodatabase.config;

import com.hridoykrisnadas.leadstodatabase.model.Person;
import com.hridoykrisnadas.leadstodatabase.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonWriter implements ItemWriter<Person> {
    private final PersonRepository personRepository;

    @Override
    public void write(Chunk<? extends Person> chunk) throws Exception {
        personRepository.saveAll(chunk);
    }
}
