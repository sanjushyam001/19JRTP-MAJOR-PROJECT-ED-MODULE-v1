package in.ashokit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ashokit.entity.Income;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

	@Query("SELECT i FROM Income i WHERE i.caseNo=?1")
	public Income findIncomeByCaseNo(Integer caseNo);
}
