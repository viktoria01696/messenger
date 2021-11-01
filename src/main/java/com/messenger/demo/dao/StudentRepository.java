package com.messenger.demo.dao;

import com.messenger.demo.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

    Page<Student> findAll(Pageable pageable);

    @Override
    Optional<Student> findById(Long id);

    Student findByLogin(String login);
}
