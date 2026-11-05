package com.example.gestionAlumni.Repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestionAlumni.Entities.Student;
import com.example.gestionAlumni.Entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
