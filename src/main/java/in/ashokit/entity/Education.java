package in.ashokit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ED_DTLS")
public class Education {
	
	@Id
	@GeneratedValue
	private Integer id;
	private Integer caseNo;
	private String status;
	private Integer year;
	private String university;
	private String highestDegree;
}
