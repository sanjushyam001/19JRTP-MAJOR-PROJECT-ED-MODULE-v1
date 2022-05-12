package in.ashokit.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.entity.EDModel;
import in.ashokit.service.EDService;

@RestController
@RequestMapping("ed-service")
public class EDRestController {

	@Autowired
	private EDService edService;
	
	@GetMapping("/case/{caseNo}")
	public ResponseEntity<?> getEligibilityDeterminationDetails(@PathVariable Integer caseNo ){
	
		return new ResponseEntity<EDModel>(edService.savePlanDetails(caseNo), HttpStatus.OK);
	}
}
