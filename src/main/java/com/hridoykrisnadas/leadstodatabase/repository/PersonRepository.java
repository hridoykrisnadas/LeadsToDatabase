package com.hridoykrisnadas.leadstodatabase.repository;

import com.hridoykrisnadas.leadstodatabase.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
