package in.ashokit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "KIDS_DTLS")
public class Kid {

	@Id
	@GeneratedValue
	private Integer id;
	
	private String name;
	
	private Integer age;
	
	private String ssn;
	
	private Integer caseNo;
}
