package in.simplygeek.retrospective.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.simplygeek.retrospective.entities.Retrospective;


@Repository
public interface RestrospectiveRepository extends JpaRepository<Retrospective, Long> {
	Page<Retrospective> findAll(Pageable pageable);
	Page<Retrospective> findByDateEquals(Date date, Pageable pageable);
    Page<Retrospective> findByDateGreaterThan(Date date, Pageable pageable);
    Page<Retrospective> findByDateLessThan(Date date, Pageable pageable);

}
