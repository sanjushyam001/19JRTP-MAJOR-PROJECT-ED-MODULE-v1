package in.ashokit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ashokit.entity.Plan;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

	@Query("SELECT p FROM Plan p WHERE p.caseNo=?1")
	public Plan findPlanByCaseNo(Integer caseNo);
}
