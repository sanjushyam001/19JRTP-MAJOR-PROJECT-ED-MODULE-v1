package in.ashokit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ashokit.entity.Education;

public interface EducationRepository extends JpaRepository<Education, Integer> {

	@Query("SELECT e FROM Education e WHERE e.caseNo=?1")
	public Education findEducationByCaseNo(Integer caseNo); 
}
