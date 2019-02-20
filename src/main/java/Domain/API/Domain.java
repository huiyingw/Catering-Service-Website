package domain.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

import domain.entityclasses.*;
import domain.util.Identifiable;
import domain.api.repo.*;

public abstract class Domain implements DomainInterface{

  private static Domain domainInstance = null;
  public static Domain getInstance(){
    if(domainInstance == null){
      domainInstance = new SQLBackedDomain();
    }
    return domainInstance;
  }

  // add new object to repos
  public void addEvent(Event newE){
    eventRepo.add(newE);
  }

  public void addEquipment(Equipment newE){
    equipmentRepo.add(newE);
  }

  public void addEmployee(Employee newE){
    employeeRepo.add(newE);
  }

  // heavy sub class usage of bellow repos
  protected RepoInterfaceTimed<Event>     eventRepo;
  protected EquipmentRepo                 equipmentRepo;
  protected EmployeeRepo                  employeeRepo;
}
