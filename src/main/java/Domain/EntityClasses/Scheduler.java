import domain.entityclasses.*;
import domain.api.Domain;
import domain.api.DomainException;
import domain.api.DomainInterface;
import domain.util.DateRange;

import java.util.*;
import java.util.ArrayList;
import java.util.Random;
import java.lang.*;

public class Scheduler{
	
	//randomize employee	
	public static ArrayList<Employee> randomEmployee(int required, ArrayList<Employee> employeeList){
		int size = employeeList.size();
		ArrayList lst = new ArrayList<Employee>(required);
		if(required > size){
			throw new IndexOutOfBoundsException();
		}else{
			Collections.shuffle(employeeList);
			for(int i=0; i < required && size>0; i++){
				lst.add(employeeList.remove(0));
			}
		}
		return lst;		
	}
	

	public void assignEmployee(Event event)throws DomainException{
		//get employee list from the database
		Domain domainInterface = Domain.getInstance();
		ArrayList<Employee> employeeList = domainInterface.filterEmployees((e) -> true);
		
		//calculating number of required employees
		//number of required employees will increase 5 for every 50 guest
		int numGuest = event.getEventDetails().numGuests;
		
		int numEmployee = (int)Math.ceil(Math.max(1,numGuest/50)) * 5;
		
		//make employee list according to the event
		ArrayList<Employee> ReqEmList = randomEmployee(numEmployee, employeeList);
		
		//assign schedule for employee		
		Employee employee = new Employee();
		int size = ReqEmList.size();
		//assign event to each employee	schedule
		for(int i=0; i<size; i++){
			employee = (Employee)ReqEmList.get(i);
			employee.addSchedulable(event);
		}	
								
	}
	
	public void assignEquip(Event event)throws DomainException{
		
		Domain domainInterface = Domain.getInstance();
		
		//get requirements for the event
		ArrayList<Long> assetList = event.getRequirements();
		ArrayList<Equipment> equipList = domainInterface.filterEquipment((e) -> true);
		
		//assign equipment according to the asset id
		for(int i=0; i<equipList.size(); i++){
			Equipment equip = equipList.get(i);
			Long assetId1 = equip.getAssetId();
			//if the (assetId of assetsDB) == (assetId of equipEventRequired)
			for(Long assetId2 :assetList){
				if(assetId1==assetId2){
					equip.addSchedulable(event);
				}
			}
		}


		
	}
			
		

}



