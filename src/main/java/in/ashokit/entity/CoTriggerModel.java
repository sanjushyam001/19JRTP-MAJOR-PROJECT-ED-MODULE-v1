package in.ashokit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "CT_DTLS")
public class CoTriggerModel {

	@Id
	@GeneratedValue
	private Integer id;
	
	private Integer caseNo;
	
	private Byte pdf;
	
	private String status;

	public CoTriggerModel(Integer caseNo, Byte pdf, String status) {
		super();
		this.caseNo = caseNo;
		this.pdf = pdf;
		this.status = status;
	}
	
	
}
