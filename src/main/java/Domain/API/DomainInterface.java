package domain.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

import domain.entityclasses.*;
import domain.util.JSONification;

public interface DomainInterface {

  // return event/equipment/employee with the given id
  public abstract Event getEventById(long id)throws DomainException;
  public abstract Equipment getEquipmentById(long id)throws DomainException;
  public abstract Employee getEmployeeById(long id)throws DomainException;

  // return all event/employee/equipment with matching name
  public abstract ArrayList<Event> getEventByName(String name) throws DomainException;
  public abstract ArrayList<Equipment> getEquipmentByName(String name) throws DomainException;
  public abstract ArrayList<Employee> getEmployeeByName(String name) throws DomainException;

  // apply custom selection filter to events/employees/equipment
  public abstract ArrayList<Event> filterEvents(Function<Event,Boolean> filter) throws DomainException;
  public abstract ArrayList<Equipment> filterEquipment(Function<Equipment,Boolean> filter) throws DomainException;
  public abstract ArrayList<Employee> filterEmployees(Function<Employee,Boolean> filter) throws DomainException;

  //return events that occure within the given range
  public abstract ArrayList<Event> getEventsInRange(Date start, Date end) throws DomainException;

  // add new object to repos
  public abstract void addEvent(Event newE);
  public abstract void addEquipment(Equipment newE);
  public abstract void addEmployee(Employee newE);

  // remove the given object
  public abstract void removeEvent(Event toRemove)throws DomainException;
  public abstract void removeEquipment(Equipment toRemove)throws DomainException;
  public abstract void removeEmployee(Employee toRemove)throws DomainException;

  // remove object with given id
  public abstract void removeEventId(long id)throws DomainException;
  public abstract void removeEquipmentId(long id)throws DomainException;
  public abstract void removeEmployeeId(long id)throws DomainException;

  public default String convertEventsToJSON(ArrayList<Event> events){
    return "[" + JSONification.eventToJSON(events) + "]";
  }
}
