package in.ashokit.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.entity.CoTriggerModel;
import in.ashokit.entity.EDModel;
import in.ashokit.entity.Education;
import in.ashokit.entity.Income;
import in.ashokit.entity.Kid;
import in.ashokit.entity.Plan;
import in.ashokit.entity.User;
import in.ashokit.repository.CoTriggerRepository;
import in.ashokit.repository.EducationRepository;
import in.ashokit.repository.EligdRepository;
import in.ashokit.repository.IncomeRepository;
import in.ashokit.repository.KidRepository;
import in.ashokit.repository.PlanRepository;
import in.ashokit.repository.UserRepository;
import in.ashokit.service.EDService;

@Service
public class EDServiceImpl implements EDService {

	@Autowired
	private EligdRepository eligdRepo;
	
	@Autowired
	private PlanRepository planRepo;
	
	@Autowired
	private IncomeRepository incomeRepo;
	
	@Autowired
	private KidRepository kidRepo;
	
	@Autowired
	private UserRepository userRepos;
	
	@Autowired
	private EducationRepository EducationRepo;
	
	@Autowired
	private CoTriggerRepository coTriggerRepos;
	
	@Override
	public EDModel savePlanDetails(Integer caseNo) {
	
		EDModel response=null;
		
			Plan plan = planRepo.findPlanByCaseNo(caseNo);
			if(plan!=null) {
				  EDModel saveEdModel = savePlanInfo(plan,eligdRepo);
				if(saveEdModel!=null) {
					response=saveEdModel;
					//Saving CoTriggerModel......
					CoTriggerModel coTriggerModel=new CoTriggerModel();
					coTriggerModel.setCaseNo(plan.getCaseNo());
					coTriggerModel.setPdf(null);
					coTriggerModel.setStatus("Pending");
					coTriggerRepos.save(coTriggerModel);
				}
				
			}
			return response;
		
	}
	private EDModel savePlanInfo(Plan plan,EligdRepository eligdRepo) {
	
		EDModel edModel=null;
		EDModel result=null;
		
		//FETCHING CASE NO ,FIRST NAME,LAST NAME AND SSN FROM USER OBJECT
		
		User user=userRepos.findUserByCaseNo(plan.getCaseNo());
		String userName=user.getFname()+" "+user.getLname();
		Integer ssn=Integer.parseInt(user.getSsn());
		
		//1#.  SNAP PLAN
		if(plan.getPlanName()!=null && !plan.getPlanName().equalsIgnoreCase("") && plan.getPlanName().equalsIgnoreCase("SNAP")) {
		
			Double totalIncome=calculateTotalIncome(incomeRepo,plan.getCaseNo());
			System.out.println("EDServiceImpl.savePlanInfo() Total SNAP Income: "+totalIncome);
			String status=totalIncome<=300?"approved":"disapproved";
			LocalDate startDate=LocalDate.now();
			LocalDate endDate=startDate.plusMonths(3);
			Double benifitAmnt=500.00;
			
			if(status.equalsIgnoreCase("approved")) {
			 edModel=new EDModel(userName,ssn,plan.getPlanName(), "approved", startDate, endDate, benifitAmnt, null);
			}else {
				edModel=new EDModel(userName,ssn,plan.getPlanName(), "disapproved", null, null, null, "Holder's income is more than 300$");
			}
			EDModel saveEdModel = eligdRepo.save(edModel);
			if(saveEdModel!=null) {
				result= saveEdModel;
			}else {
				result= null;
			}
		}
		
		//#2. CCAP PLAN
		
		if(plan.getPlanName()!=null && !plan.getPlanName().equalsIgnoreCase("") && plan.getPlanName().equalsIgnoreCase("CCAP")){
			boolean iskidAgeValid=true;
			Double totalIncome=calculateTotalIncome(incomeRepo,plan.getCaseNo());
			
			List<Kid> kidsList = kidRepo.findKidByCaseNo(plan.getCaseNo());
			Integer noOfKids=kidsList.size();
			for(Kid kid:kidsList) {
				if(kid.getAge()>16||kid.getAge()<0) {
					iskidAgeValid=false;
					break;
				}
				
			}
			System.out.println("VALID AGE ? :::: "+iskidAgeValid +"NO OF KIDS :: "+noOfKids);
			String status=(totalIncome<=300) && (noOfKids>0&&iskidAgeValid==true)?"approved":"disapproved";
			LocalDate startDate=LocalDate.now();
			LocalDate endDate=startDate.plusMonths(3);
			Double benifitAmnt=600.00;
			
			if(status.equalsIgnoreCase("approved")) {
				 edModel=new EDModel(userName,ssn,plan.getPlanName(), "approved", startDate, endDate, benifitAmnt, null);
				}else {
					String denialMesg="Holder's income is more than 300$ or Kids age greater than 16 Or you dont have kids";
					edModel=new EDModel(userName,ssn,plan.getPlanName(), "disapproved", null, null, null, denialMesg);
				}
				EDModel saveEdModel = eligdRepo.save(edModel);
				if(saveEdModel!=null) {
					result= saveEdModel;
				}else {
					result= null;
				}
			}
		
		//#4 MEDICAID PLAN
		
		if(plan.getPlanName()!=null && !plan.getPlanName().equalsIgnoreCase("") && plan.getPlanName().equalsIgnoreCase("MEDICAID")){
			
			Double totalIncome=calculateTotalIncome(incomeRepo,plan.getCaseNo());
			Income income = incomeRepo.findIncomeByCaseNo(plan.getCaseNo());
			String status=(totalIncome<=300) && (income.getProperty()==0)?"approved":"disapproved";
			LocalDate startDate=LocalDate.now();
			LocalDate endDate=startDate.plusMonths(3);
			Double benifitAmnt=600.00;
			
			if(status.equalsIgnoreCase("approved")) {
				 edModel=new EDModel(userName,ssn,plan.getPlanName(), "approved", startDate, endDate, benifitAmnt, null);
				}else {
					String denialMesg="Holder's income is more than 300$ or Property is more than zero";
					edModel=new EDModel(userName,ssn,plan.getPlanName(), "disapproved", null, null, null, denialMesg);
				}
				EDModel saveEdModel = eligdRepo.save(edModel);
				if(saveEdModel!=null) {
					result= saveEdModel;
				}else {
					result= null;
				}
			}
		
		//#5 MEDICARE PLAN
		
		if(plan.getPlanName()!=null && !plan.getPlanName().equalsIgnoreCase("") && plan.getPlanName().equalsIgnoreCase("MEDICARE")){
			
//			User user=userRepos.findUserByCaseNo(plan.getCaseNo());
//			String userName=user.getFname()+" "+user.getLname();
//			Integer ssn=Integer.parseInt(user.getSsn());
			Integer age= calculateAge(user.getDob());
			String status=age>=65?"approved":"disapproved";
			LocalDate startDate=LocalDate.now();
			LocalDate endDate=startDate.plusMonths(3);
			Double benifitAmnt=600.00;
			
			if(status.equalsIgnoreCase("approved")) {
				 edModel=new EDModel(userName,ssn,plan.getPlanName(), "approved", startDate, endDate, benifitAmnt, null);
				}else {
					String denialMesg="Holder's income is more than 300$ or Property is more than zero";
					edModel=new EDModel(userName,ssn,plan.getPlanName(), "disapproved", null, null, null, denialMesg);
				}
				EDModel saveEdModel = eligdRepo.save(edModel);
				if(saveEdModel!=null) {
					result= saveEdModel;
				}else {
					result= null;
				}
			}
			// #7. NJW PLAN
		
			if(plan.getPlanName()!=null && !plan.getPlanName().equalsIgnoreCase("") && plan.getPlanName().equalsIgnoreCase("NJW")){
			Education education = EducationRepo.findEducationByCaseNo(plan.getCaseNo());
			String educationStatus = education.getStatus();
			String status=educationStatus.equalsIgnoreCase("Graduated")?"approved":"disapproved";
			LocalDate startDate=LocalDate.now();
			LocalDate endDate=startDate.plusMonths(3);
			Double benifitAmnt=600.00;
			
			if(status.equalsIgnoreCase("approved")) {
				 edModel=new EDModel(userName,ssn,plan.getPlanName(), "approved", startDate, endDate, benifitAmnt, null);
				}else {
					String denialMesg="Holder is not graduated";
					edModel=new EDModel(userName,ssn,plan.getPlanName(), "disapproved", null, null, null, denialMesg);
				}
				EDModel saveEdModel = eligdRepo.save(edModel);
				if(saveEdModel!=null) {
					result= saveEdModel;
				}else {
					result= null;
				}
			}

		return result;
		
	}
	//CALCULATE TOTAL INCOME OF APPLICANT
	
	private Double calculateTotalIncome(IncomeRepository incomeRepo, Integer caseNo) {
		Income income = incomeRepo.findIncomeByCaseNo(caseNo);
		return income.getProperty()+income.getRent()+income.getSalary();
	}
	//CALCULATE AGE BY USER'S DOB
	private Integer calculateAge(LocalDate dob)   
	{  
		//creating an instance of the LocalDate class and invoking the now() method      
		//now() method obtains the current date from the system clock in the default time zone      
			LocalDate curDate = LocalDate.now();  
		//calculates the amount of time between two dates and returns the years  
			if ((dob != null) && (curDate != null))   
			{  
				return Period.between(dob, curDate).getYears();  
			}  
			else  
			{  
				return 0;  
			}  
	}

	

}
