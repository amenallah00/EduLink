package com.example.gestionAlumni;

import com.example.gestionAlumni.Entities.Administrator;
import com.example.gestionAlumni.Entities.Alumni;
import com.example.gestionAlumni.Entities.Recruiter;
import com.example.gestionAlumni.Entities.Student;
import com.example.gestionAlumni.Repos.AdminRepository;
import com.example.gestionAlumni.Repos.AlumniRepository;
import com.example.gestionAlumni.Repos.RecruiterRepository;
import com.example.gestionAlumni.Repos.StudentRepository;
import com.example.gestionAlumni.Repos.CompanyReviewRepository;
import com.example.gestionAlumni.Repos.KnowledgeBaseRepository;
import com.example.gestionAlumni.Entities.CompanyReview;
import com.example.gestionAlumni.Entities.KnowledgeBase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

public class DataSeeder implements CommandLineRunner {

    private final AlumniRepository alumniRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final RecruiterRepository recruiterRepository;
    private final CompanyReviewRepository companyReviewRepository;
    private final KnowledgeBaseRepository knowledgeBaseRepository;
    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;

    public DataSeeder(AlumniRepository alumniRepository, StudentRepository studentRepository, AdminRepository adminRepository, RecruiterRepository recruiterRepository, CompanyReviewRepository companyReviewRepository, KnowledgeBaseRepository knowledgeBaseRepository, PasswordEncoder passwordEncoder) {
        this.alumniRepository = alumniRepository;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.recruiterRepository = recruiterRepository;
        this.companyReviewRepository = companyReviewRepository;
        this.knowledgeBaseRepository = knowledgeBaseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            entityManager.createNativeQuery("UPDATE user SET dtype='etudiant' WHERE dtype='Student'").executeUpdate();
            entityManager.createNativeQuery("UPDATE user SET dtype='alumni' WHERE dtype='Alumni'").executeUpdate();
            entityManager.createNativeQuery("UPDATE user SET dtype='admin' WHERE dtype='Administrator'").executeUpdate();
            entityManager.createNativeQuery("UPDATE user SET dtype='recruiter' WHERE dtype='Recruiter'").executeUpdate();
            alumniRepository.findByEmail("jean.dupont@example.com").ifPresentOrElse(
                a -> {
                    a.setExpertise("Expert en développement Java et architectures microservices. Passionné par le Cloud et les bonnes pratiques de code (Clean Code, TDD).");
                    a.setAcademicPath("Diplômé en Génie Logiciel en 2020. Major de promotion.");
                    a.setProfessionalPath("2 ans chez Sopra Steria avant de rejoindre Microsoft en tant que Senior Engineer.");
                    a.setSkills(Arrays.asList("Java", "Spring Boot", "Angular", "Azure"));
                    a.setOpenToMentoring(true);
                    a.setVerified(true);
                    a.setActive(true);
                    alumniRepository.save(a);
                },
                () -> {
                    Alumni a = new Alumni();
                    a.setFirstName("Jean");
                    a.setLastName("Dupont");
                    a.setEmail("jean.dupont@example.com");
                    a.setPassword(passwordEncoder.encode("password123"));
                    a.setGraduationYear(2020);
                    a.setCurrentCompany("Microsoft");
                    a.setCurrentJob("Senior Software Engineer");
                    a.setSector("Informatique");
                    a.setSpeciality("Informatique / Génie Logiciel");
                    a.setSkills(Arrays.asList("Java", "Spring Boot", "Angular", "Azure"));
                    a.setOpenToMentoring(true);
                    a.setActive(true);
                    a.setVerified(true);
                    a.setExpertise("Expert en développement Java et architectures microservices. Passionné par le Cloud et les bonnes pratiques de code (Clean Code, TDD).");
                    a.setAcademicPath("Diplômé en Génie Logiciel en 2020. Major de promotion.");
                    a.setProfessionalPath("2 ans chez Sopra Steria avant de rejoindre Microsoft en tant que Senior Engineer.");
                    alumniRepository.save(a);
                }
            );

            alumniRepository.findByEmail("marie.curie@example.com").ifPresentOrElse(
                a -> {
                    a.setExpertise("Cloud Architect spécialisée dans les infrastructures scalables et Kubernetes. J'aide les entreprises à migrer vers le Cloud.");
                    a.setAcademicPath("Master en Systèmes et Réseaux, 2018.");
                    a.setProfessionalPath("Passée par plusieurs startups avant d'intégrer l'équipe Cloud de Google.");
                    a.setSkills(Arrays.asList("Python", "Go", "Docker", "Kubernetes", "Java"));
                    a.setOpenToMentoring(true);
                    a.setVerified(true);
                    a.setActive(true);
                    alumniRepository.save(a);
                },
                () -> {
                    Alumni a = new Alumni();
                    a.setFirstName("Marie");
                    a.setLastName("Curie");
                    a.setEmail("marie.curie@example.com");
                    a.setPassword(passwordEncoder.encode("password123"));
                    a.setGraduationYear(2018);
                    a.setCurrentCompany("Google");
                    a.setCurrentJob("Cloud Architect");
                    a.setSector("Informatique");
                    a.setSpeciality("Cloud / Infrastructure");
                    a.setSkills(Arrays.asList("Python", "Go", "Docker", "Kubernetes", "Java"));
                    a.setOpenToMentoring(true);
                    a.setActive(true);
                    a.setVerified(true);
                    a.setExpertise("Cloud Architect spécialisée dans les infrastructures scalables et Kubernetes. J'aide les entreprises à migrer vers le Cloud.");
                    a.setAcademicPath("Master en Systèmes et Réseaux, 2018.");
                    a.setProfessionalPath("Passée par plusieurs startups avant d'intégrer l'équipe Cloud de Google.");
                    alumniRepository.save(a);
                }
            );

            alumniRepository.findByEmail("thomas.sankara@example.com").ifPresentOrElse(
                a -> {
                    a.setExpertise("Spécialiste Data Science et Analyse de données financières. Maîtrise de Python, R et des modèles de Machine Learning.");
                    a.setAcademicPath("Doctorat en Intelligence Artificielle Appliquée à la Finance, 2015.");
                    a.setProfessionalPath("Analyste chez BNP Paribas puis Data Scientist Senior à la Société Générale.");
                    a.setSkills(Arrays.asList("Python", "R", "SQL", "Machine Learning", "Java"));
                    a.setOpenToMentoring(true);
                    a.setVerified(true);
                    a.setActive(true);
                    alumniRepository.save(a);
                },
                () -> {
                    Alumni a = new Alumni();
                    a.setFirstName("Thomas");
                    a.setLastName("Sankara");
                    a.setEmail("thomas.sankara@example.com");
                    a.setPassword(passwordEncoder.encode("password123"));
                    a.setGraduationYear(2015);
                    a.setCurrentCompany("Société Générale");
                    a.setCurrentJob("Data Scientist");
                    a.setSector("Finance / IT");
                    a.setSpeciality("Informatique / Data Science");
                    a.setSkills(Arrays.asList("Python", "R", "SQL", "Machine Learning", "Java"));
                    a.setOpenToMentoring(true);
                    a.setActive(true);
                    a.setVerified(true);
                    a.setExpertise("Spécialiste Data Science et Analyse de données financières. Maîtrise de Python, R et des modèles de Machine Learning.");
                    a.setAcademicPath("Doctorat en Intelligence Artificielle Appliquée à la Finance, 2015.");
                    a.setProfessionalPath("Analyste chez BNP Paribas puis Data Scientist Senior à la Société Générale.");
                    alumniRepository.save(a);
                }
            );

            alumniRepository.findByEmail("sophie.martin@example.com").ifPresentOrElse(
                a -> {
                    a.setExpertise("Développeuse Backend passionnée par les systèmes distribués. Expertise en Java, AWS et bases de données NoSQL.");
                    a.setAcademicPath("Licence en Informatique Fondamentale, 2021.");
                    a.setProfessionalPath("Stage de fin d'études chez Amazon, recrutée en CDI immédiatement après.");
                    a.setSkills(Arrays.asList("Java", "AWS", "DynamoDB"));
                    a.setOpenToMentoring(true);
                    a.setVerified(true);
                    a.setActive(true);
                    alumniRepository.save(a);
                },
                () -> {
                    Alumni a = new Alumni();
                    a.setFirstName("Sophie");
                    a.setLastName("Martin");
                    a.setEmail("sophie.martin@example.com");
                    a.setPassword(passwordEncoder.encode("password123"));
                    a.setGraduationYear(2021);
                    a.setCurrentCompany("Amazon");
                    a.setCurrentJob("Software Engineer");
                    a.setSector("Informatique");
                    a.setSpeciality("Backend Engineering");
                    a.setSkills(Arrays.asList("Java", "AWS", "DynamoDB"));
                    a.setOpenToMentoring(true);
                    a.setActive(true);
                    a.setVerified(true);
                    a.setExpertise("Développeuse Backend passionnée par les systèmes distribués. Expertise en Java, AWS et bases de données NoSQL.");
                    a.setAcademicPath("Licence en Informatique Fondamentale, 2021.");
                    a.setProfessionalPath("Stage de fin d'études chez Amazon, recrutée en CDI immédiatement après.");
                    alumniRepository.save(a);
                }
            );

            alumniRepository.findByEmail("lucas.dubois@example.com").ifPresentOrElse(
                a -> {
                    a.setExpertise("Consultant en transformation digitale. J'accompagne les grands comptes dans leur agilité et leur modernisation IT.");
                    a.setAcademicPath("Promotion 2019, Spécialité Management des SI.");
                    a.setProfessionalPath("3 ans d'expérience en conseil stratégique chez Capgemini.");
                    a.setSkills(Arrays.asList("Agile", "Scrum", "Management"));
                    a.setOpenToMentoring(true);
                    a.setVerified(true);
                    a.setActive(true);
                    alumniRepository.save(a);
                },
                () -> {
                    Alumni a = new Alumni();
                    a.setFirstName("Lucas");
                    a.setLastName("Dubois");
                    a.setEmail("lucas.dubois@example.com");
                    a.setPassword(passwordEncoder.encode("password123"));
                    a.setGraduationYear(2019);
                    a.setCurrentCompany("Capgemini");
                    a.setCurrentJob("Consultant IT");
                    a.setSector("Conseil");
                    a.setSpeciality("Transformation Digitale");
                    a.setSkills(Arrays.asList("Agile", "Scrum", "Management"));
                    a.setOpenToMentoring(true);
                    a.setActive(true);
                    a.setVerified(true);
                    a.setExpertise("Consultant en transformation digitale. J'accompagne les grands comptes dans leur agilité et leur modernisation IT.");
                    a.setAcademicPath("Promotion 2019, Spécialité Management des SI.");
                    a.setProfessionalPath("3 ans d'expérience en conseil stratégique chez Capgemini.");
                    alumniRepository.save(a);
                }
            );

            studentRepository.findByEmail("test@student.com").ifPresentOrElse(
                s -> {
                    s.setPassword(passwordEncoder.encode("password123"));
                    studentRepository.save(s);
                    System.out.println("STUDENT USER PASSWORD RESET");
                },
                () -> {
                    Student student = new Student();
                    student.setFirstName("Test");
                    student.setLastName("Student");
                    student.setEmail("test@student.com");
                    student.setPassword(passwordEncoder.encode("password123"));
                    student.setActive(true);
                    student.setVerified(true);
                    student.setGraduationYear(2025);
                    student.setAverage(15.5f);
                    student.setSpeciality("Software Engineering");
                    student.setPredictedGradYear(2025);
                    studentRepository.save(student);
                    System.out.println("DEFAULT STUDENT USER CREATED");
                }
            );

            adminRepository.findByEmail("test@admin.com").ifPresentOrElse(
                a -> {
                    a.setPassword(passwordEncoder.encode("password123"));
                    adminRepository.save(a);
                    System.out.println("ADMIN USER PASSWORD RESET");
                },
                () -> {
                    Administrator admin = new Administrator();
                    admin.setFirstName("Super");
                    admin.setLastName("Admin");
                    admin.setEmail("test@admin.com");
                    admin.setPassword(passwordEncoder.encode("password123"));
                    admin.setActive(true);
                    admin.setGraduationYear(0);
                    adminRepository.save(admin);
                    System.out.println("DEFAULT ADMIN USER CREATED");
                }
            );

            if (alumniRepository.findByEmail("test@alumni.com").isEmpty()) {
                Alumni alumni = new Alumni();
                alumni.setFirstName("Test");
                alumni.setLastName("Alumni");
                alumni.setEmail("test@alumni.com");
                alumni.setPassword(passwordEncoder.encode("password123"));
                alumni.setGraduationYear(2020);
                alumni.setActive(true);
                alumni.setVerified(true);
                alumniRepository.save(alumni);
                System.out.println("DEFAULT ALUMNI USER CREATED");
            } else {
                alumniRepository.findByEmail("test@alumni.com").ifPresent(a -> {
                    a.setPassword(passwordEncoder.encode("password123"));
                    a.setVerified(true);
                    a.setActive(true);
                    alumniRepository.save(a);
                    System.out.println("ALUMNI USER PASSWORD RESET");
                });
            }

            if (recruiterRepository.findByEmail("test@recruiter.com").isEmpty()) {
                Recruiter recruiter = new Recruiter();
                recruiter.setFirstName("Test");
                recruiter.setLastName("Recruiter");
                recruiter.setEmail("test@recruiter.com");
                recruiter.setPassword(passwordEncoder.encode("password123"));
                recruiter.setCompanyName("Test Corp");
                recruiter.setGraduationYear(0);
                recruiter.setActive(true);
                recruiterRepository.save(recruiter);
                System.out.println("DEFAULT RECRUITER USER CREATED");
            } else {
                recruiterRepository.findByEmail("test@recruiter.com").ifPresent(r -> {
                    r.setPassword(passwordEncoder.encode("password123"));
                    r.setActive(true);
                    recruiterRepository.save(r);
                    System.out.println("RECRUITER USER PASSWORD RESET");
                });
            }
            
            Alumni defaultAlumni = alumniRepository.findByEmail("test@alumni.com").orElse(null);
            if (defaultAlumni != null && companyReviewRepository.count() == 0) {
                CompanyReview cr1 = new CompanyReview();
                cr1.setCompanyName("Orange");
                cr1.setRating(4);
                cr1.setCulture("Bonne culture d'entreprise, axée sur l'équilibre pro/perso.");
                cr1.setRecruitmentProcess("Processus standard : 1 test technique, 2 entretiens RH et manager.");
                cr1.setInterviewTips("Révisez bien vos bases en réseaux et développement Java.");
                cr1.setMissions("Développement de microservices pour le portail client.");
                cr1.setWorkAtmosphere("Très collaborative mais parfois un peu hiérarchique grand groupe.");
                cr1.setGrowthOpportunities("Nombreuses opportunités de mobilité interne.");
                cr1.setPros("CE génial, bons locaux, télétravail flexible.");
                cr1.setCons("Salaires d'entrée parfois justes par rapport aux startups.");
                cr1.setAuthor(defaultAlumni);
                companyReviewRepository.save(cr1);

                CompanyReview cr2 = new CompanyReview();
                cr2.setCompanyName("Sopra Steria");
                cr2.setRating(3);
                cr2.setCulture("Beaucoup de missions diverses, ambiance jeune.");
                cr2.setRecruitmentProcess("Très rapide, souvent en 1 ou 2 entretiens maximum.");
                cr2.setInterviewTips("Montrez que vous êtes adaptable et prêt à apprendre de nouvelles technos.");
                cr2.setMissions("Consultant technique sur des gros projets pour le secteur public.");
                cr2.setWorkAtmosphere("Dépend fortement de la mission et du client final.");
                cr2.setGrowthOpportunities("Évolution rapide possible si on s'investit sérieusement.");
                cr2.setPros("Excellente école pour débuter, diversité des projets.");
                cr2.setCons("Beaucoup de turnover, package salarial dans la moyenne basse.");
                cr2.setAuthor(defaultAlumni);
                companyReviewRepository.save(cr2);

                CompanyReview cr3 = new CompanyReview();
                cr3.setCompanyName("Google");
                cr3.setRating(5);
                cr3.setCulture("Exceptionnelle, axée sur l'innovation et le bien-être.");
                cr3.setRecruitmentProcess("Long et exigeant (4 ou 5 étapes incluant coding interviews).");
                cr3.setInterviewTips("Pratiquez énormément sur LeetCode, maîtrisez les algorithmes.");
                cr3.setMissions("Optimisation de l'infrastructure cloud.");
                cr3.setWorkAtmosphere("Stipulante, entouré de personnes très brillantes.");
                cr3.setGrowthOpportunities("Carrière internationale, possibilités illimitées.");
                cr3.setPros("Avantages sociaux hors normes, salaire excellent, prestige.");
                cr3.setCons("Pression liée aux performances attendues, syndrome de l'imposteur.");
                cr3.setAuthor(defaultAlumni);
                companyReviewRepository.save(cr3);
                
                System.out.println("COMPANY REVIEWS SEEDED");
            }
            
            if (defaultAlumni != null && knowledgeBaseRepository.count() == 0) {
                // KB 1
                KnowledgeBase kb1 = new KnowledgeBase();
                kb1.setCategory("Testimony");
                kb1.setTitle("Mon expérience d'intégration chez Orange");
                kb1.setContent("Le parcours d'intégration chez Orange est très bien structuré. Dès le premier jour, on reçoit tout le matériel nécessaire et un buddy nous est assigné pour faciliter notre arrivée dans l'équipe. Les formations internes sont très utiles !");
                kb1.setAnonymous(false);
                kb1.setCompanyName("Orange");
                kb1.setAuthor(defaultAlumni);
                knowledgeBaseRepository.save(kb1);

                // KB 2
                KnowledgeBase kb2 = new KnowledgeBase();
                kb2.setCategory("FAQ");
                kb2.setTitle("Comment se préparer aux entretiens de SSII (ESN) comme Sopra ?");
                kb2.setContent("Pour réussir en ESN, il faut mettre en avant : 1. Votre capacité d'adaptation. 2. Votre aisance relationnelle (très important face au client). 3. Votre rigueur technique sur 1 ou 2 technos clés. Ne négligez pas la posture de consultant lors des mises en situation.");
                kb2.setAnonymous(false);
                kb2.setCompanyName("Sopra Steria");
                kb2.setAuthor(defaultAlumni);
                knowledgeBaseRepository.save(kb2);
                
                // KB 3
                KnowledgeBase kb3 = new KnowledgeBase();
                kb3.setCategory("Sector Tips");
                kb3.setTitle("Réussir les 'System Design Interviews' chez les GAFAM");
                kb3.setContent("Chez Google ou Amazon, le sys design est crucial. Pensez toujours aux aspects suivants : Scalability (mise à l'échelle), Reliability (fiabilité), Availability (disponibilité). Dessinez l'architecture de haut niveau avant de plonger dans les détails. Posez toujours des questions au recruteur pour définir le scope exact du problème !");
                kb3.setAnonymous(true);
                kb3.setCompanyName("Google");
                kb3.setAuthor(defaultAlumni);
                knowledgeBaseRepository.save(kb3);
                
                System.out.println("KNOWLEDGE BASE SEEDED");
            }

        } catch (Exception e) {
            System.err.println("ERROR SEEDING DATA: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
