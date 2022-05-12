package in.ashokit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "PLAN_DTLS")
public class Plan {

	@Id
	@GeneratedValue
	private Integer id;
	private Integer caseNo;
	private String planName;
	
}
