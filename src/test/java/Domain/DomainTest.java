import org.junit.*;
import java.util.Date;
import java.util.ArrayList;

import domain.api.Domain;
import domain.api.DomainException;
import domain.entityclasses.*;
import domain.util.*;

import database.api.DebugDBInterface;
import database.jdbc.JDBCConnectionType;

public class DomainTest{

  @Test
  public void generalTest() throws Exception{
    Domain domainInterface = Domain.getInstance();
    init(domainInterface);
    initDBValues();

    //tests
    eventRecall(domainInterface);
    equipmentRecall(domainInterface);
    employeeRecall(domainInterface);
    filterCheck(domainInterface);
    evenRangeCheck(domainInterface);
    deleteEvents(domainInterface);
  }

  private void init(Domain domainInterface) throws DomainException{
    //add events
    Event wedding = new Event("Wedding", new Date(2017 - 1900, 10,25,0,0,0),new Date(2017 - 1900, 10,29,0,0,0));
    wedding.setId(1);
    Event divorce = new Event("divorce", new Date(2018 - 1900, 10,25,0,0,0),new Date(2018 - 1900, 10,29,0,0,0));
    divorce.setId(2);
    domainInterface.addEvent(wedding);
    domainInterface.addEvent(divorce);

    //add equipment
    Equipment cake = new Equipment("Cake", 1);
    Equipment plates = new Equipment("White Plates", 2);
    domainInterface.addEquipment(cake);
    domainInterface.addEquipment(plates);

    //add employees
    Employee bob = new Employee("Bob", 1);
    Employee jane = new Employee("Jane", 2);
    domainInterface.addEmployee(bob);
    domainInterface.addEmployee(jane);
  }

  private void initDBValues() throws Exception{
    DebugDBInterface dbI = DebugDBInterface.getInstance();
    dbI.ensureConnection(JDBCConnectionType.SQLITE);
    dbI.NUKE();
    dbI.initializeDB();

    Event onDisk = new Event("Disk",new Date(2017 - 1900, 11,1,0,0,0),new Date(2017 - 1900, 11,29,0,0,0));
    onDisk.setId(666);
    Event onDiskName = new Event("DiskName",new Date(2017 - 1900, 11,25,0,0,0),new Date(2017 - 1900, 11,30,0,0,0));
    onDiskName.setId(667);
    dbI.store(onDisk);
    dbI.store(onDiskName);
  }

  private void eventRecall(Domain domainInterface) throws DomainException{
    Event wedding = domainInterface.getEventById(1);
    assert(wedding.getName().equals("Wedding"));
    assert(wedding.getDateRange().getStart() != null);
    assert(wedding.getDateRange().getEnd() != null);

    ArrayList<Event> divorce = domainInterface.getEventByName("divorce");
    assert(divorce.size() == 1);
    assert(divorce.get(0).getName().equals("divorce"));

    Event diskEvent = domainInterface.getEventById(666);
    assert(diskEvent.getName().equals("Disk"));

    ArrayList<Event> diskNameEvent = domainInterface.getEventByName("DiskName");
    assert(diskNameEvent.size() == 1);
    assert(diskNameEvent.get(0).getName().equals("DiskName"));
  }

  private void equipmentRecall(Domain domainInterface) throws DomainException {
    Equipment cake = domainInterface.getEquipmentById(1);
    assert(cake.getName().equals("Cake"));

    ArrayList<Equipment> plates = domainInterface.getEquipmentByName("White Plates");
    assert(plates.size() == 1);
    assert(plates.get(0).getName().equals("White Plates"));
  }

  private void employeeRecall(Domain domainInterface) throws DomainException {
    Employee bob = domainInterface.getEmployeeById(1);
    assert(bob.getName().equals("Bob"));

    ArrayList<Employee> jane = domainInterface.getEmployeeByName("Jane");
    assert(jane.size() == 1);
    assert(jane.get(0).getName().equals("Jane"));
  }

  private void filterCheck(Domain domainInterface) throws DomainException{
    ArrayList<Event> events = domainInterface.filterEvents((e) -> true);
    assert(events.size() == 4);
    events = domainInterface.filterEvents((e) -> e.getId() < 100);
    assert(events.size() == 2);
    events = domainInterface.filterEvents((e) -> e.getId() > 100);
    assert(events.size() == 2);
    assert(events.get(0).getId() > 100);

    ArrayList<Equipment> equipment = domainInterface.filterEquipment((e) -> true);
    assert(equipment.size() == 2);

    ArrayList<Employee> employees = domainInterface.filterEmployees((e) -> true);
    assert(employees.size() == 2);
  }

  private void evenRangeCheck(Domain domainInterface) throws DomainException{
    ArrayList<Event> events = domainInterface.getEventsInRange(
        new Date(2017 - 1900, 10,25,0,0,0), new Date(2017 - 1900, 11,10,0,0,0));
    assert(events.size() == 2);
    events = domainInterface.getEventsInRange(
        new Date(2017 - 1900, 10,25,0,0,0), new Date(2017 - 1900, 10,30,0,0,0));
    assert(events.size() == 1);

    events = domainInterface.getEventsInRange(
        new Date(2015 - 1900, 10,25,0,0,0), new Date(2015 - 1900, 11,10,0,0,0));
    assert(events == null);
  }

  private void deleteEvents(Domain domainInterface) throws DomainException{
    domainInterface.removeEventId(666);
    domainInterface.removeEventId(667);

    Event event = domainInterface.getEventById(666);
    assert(event == null);
    event = domainInterface.getEventById(667);
    assert(event == null);
  }

}
