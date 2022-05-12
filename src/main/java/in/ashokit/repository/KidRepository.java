package in.ashokit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.ashokit.entity.Kid;

@Repository
public interface KidRepository extends JpaRepository<Kid, Integer>{

	@Query("SELECT k FROM Kid k WHERE k.caseNo=?1")
	public List<Kid> findKidByCaseNo(Integer caseNo);
}
