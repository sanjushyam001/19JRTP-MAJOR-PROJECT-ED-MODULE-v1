package in.ashokit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "INCOME_DTLS")
public class Income {

	@Id
	@GeneratedValue
	private Integer id;
	private Integer caseNo;
	private Double salary;
	private Double property;
	private Double rent;
}
