package adaptivelearning.module.certificates.repository;

import adaptivelearning.module.certificates.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Optional<Certificate> findByCode(String code);

    List<Certificate> findByNameContainingIgnoreCase(String name);

    boolean existsByCode(String code);
}
