package com.example.gestionAlumni.Repos;



import com.example.gestionAlumni.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni,Long> {

    Optional<Alumni> findByEmail(String email);
    Optional<Alumni> findByVerificationToken(String token);

    Boolean existsByEmail(String email);

    List<Alumni> findByGraduationYear(int graduationYear);

    List<Alumni> findByCurrentCompany(String company);

    List<Alumni> findByCurrentJob(String currentJob);

    List<Alumni> findBySalaryGreaterThanEqual(Long salary);

    List<Alumni> findBySalaryLessThanEqual(Long salary);

    List<Alumni> findBySalary(Long salary);

    List<Alumni> findBySpeciality(String speciality);

    List<Alumni> findByGraduationYearAndCurrentCompany(int graduationYear, String company);

    List<Alumni> findBySector(String sector);

    @Query("SELECT a FROM Alumni a JOIN a.skills s WHERE s = :skill")
    List<Alumni> findBySkill(String skill);

    List<Alumni> findByVerifiedTrue();

    List<Alumni> findByVerifiedFalse();

    List<Alumni> findByVerifiedTrueAndOpenToMentoringTrue();

    @Query("SELECT DISTINCT a FROM Alumni a LEFT JOIN a.skills s WHERE a.verified = true " +
           "AND (:query IS NULL OR LOWER(a.firstName) LIKE LOWER(CONCAT('%',:query,'%')) " +
           "OR LOWER(a.lastName) LIKE LOWER(CONCAT('%',:query,'%')) " +
           "OR LOWER(a.currentJob) LIKE LOWER(CONCAT('%',:query,'%')) " +
           "OR LOWER(a.currentCompany) LIKE LOWER(CONCAT('%',:query,'%'))) " +
           "AND (:sector IS NULL OR :sector = '' OR LOWER(a.sector) = LOWER(:sector)) " +
           "AND (:company IS NULL OR :company = '' OR LOWER(a.currentCompany) = LOWER(:company)) " +
           "AND (:graduationYear IS NULL OR a.graduationYear = :graduationYear) " +
           "AND (:skill IS NULL OR :skill = '' OR LOWER(s) = LOWER(:skill))")
    List<Alumni> searchDirectory(@Param("query") String query,
                                @Param("sector") String sector,
                                @Param("company") String company,
                                @Param("graduationYear") Integer graduationYear,
                                @Param("skill") String skill);

    @Query("SELECT DISTINCT a FROM Alumni a LEFT JOIN a.skills s WHERE a.verified = true AND a.openToMentoring = true " +
           "AND (:company IS NULL OR :company = '' OR LOWER(a.currentCompany) LIKE LOWER(CONCAT('%',:company,'%'))) " +
           "AND (:job IS NULL OR :job = '' OR LOWER(a.currentJob) LIKE LOWER(CONCAT('%',:job,'%'))) " +
           "AND (:sector IS NULL OR :sector = '' OR LOWER(a.sector) LIKE LOWER(CONCAT('%',:sector,'%'))) " +
           "AND (:speciality IS NULL OR :speciality = '' OR LOWER(a.speciality) LIKE LOWER(CONCAT('%',:speciality,'%'))) " +
           "AND (:skill IS NULL OR :skill = '' OR LOWER(s) LIKE LOWER(CONCAT('%',:skill,'%')))")
    List<Alumni> searchMentors(@Param("company") String company,
                                @Param("job") String job,
                                @Param("sector") String sector,
                                @Param("speciality") String speciality,
                                @Param("skill") String skill);

    @Query("SELECT a.applications FROM Alumni a WHERE a.id = :alumniId")
    List<Application> findApplicationsByAlumniId(Long alumniId);

    @Query("SELECT a.mentorshipRequests FROM Alumni a WHERE a.id = :alumniId")
    List<MentorshipRequest> findMentorshipRequestsByAlumniId(Long alumniId);

    @Query("SELECT a.internshipRequestsReceived FROM Alumni a WHERE a.id = :alumniId")
    List<InternshipRequest> findInternshipRequestsByAlumniId(Long alumniId);
}