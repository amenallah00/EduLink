package com.example.gestionAlumni.Repos;



import com.example.gestionAlumni.Entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Administrator,Long> {

    Optional<Administrator> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<Administrator> findAllByActiveTrue();

    List<Administrator> findAllByActiveFalse();

    @Modifying
    @Query("UPDATE Administrator a SET a.active = :status WHERE a.id IN :ids")
    int updateActiveStatus(@Param("ids") List<Long> ids, @Param("status") boolean status);

}