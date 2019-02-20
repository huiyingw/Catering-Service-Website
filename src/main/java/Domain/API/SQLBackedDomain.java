package domain.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Supplier;

import domain.entityclasses.*;
import domain.api.repo.*;
import domain.util.DynamicLookupQuery;
import domain.util.AdvancedCast;
import domain.util.Identifiable;
import domain.util.DateRange;
import domain.util.EventRangeQuery;
import database.query.StorableInterface;
import database.query.StorageDriverException;
import database.jdbc.JDBCException;
import database.api.DefaultDBInterface;

class SQLBackedDomain extends Domain {

  public SQLBackedDomain(){
    super();
    eventRepo     = new EventRepo();
    equipmentRepo = new EquipmentRepo();
    employeeRepo  = new EmployeeRepo();
  }

  // return event/equipment/employee with the given id
  @Override
  public Event getEventById(final long id) throws DomainException{
    return (Event)DataBaseCacher.checkDBOnFailure(eventRepo.getById(id),() -> new Event(id));
  }
  @Override
  public Equipment getEquipmentById(long id)throws DomainException{
    return (Equipment)DataBaseCacher.checkDBOnFailure(equipmentRepo.getById(id),
              () -> new Equipment("FOO",id));
  }
  @Override
  public Employee getEmployeeById(long id)throws DomainException{
    return (Employee)DataBaseCacher.checkDBOnFailure(employeeRepo.getById(id),
              () -> new Employee("FOO",id));
  }

  // return all event/employee/equipment with matching name
  @Override
  public ArrayList<Event> getEventByName(String name) throws DomainException{
    ArrayList<Event> res = eventRepo.getByName(name);
    //pull up more results from db.
    ArrayList<Event> resDB = DataBaseCacher.<Event>getByNameQuery(name,"events",(id)->new Event(id));
    return mergeArrays(res,resDB);
  }
  @Override
  public ArrayList<Equipment> getEquipmentByName(String name) throws DomainException{
    ArrayList<Equipment> res = equipmentRepo.getByName(name);
    //pull up more results from db.
    ArrayList<Equipment> resDB = DataBaseCacher.<Equipment>getByNameQuery(name,"equipment",(id)->new Equipment("f",id));
    return mergeArrays(res,resDB);
  }
  @Override
  public ArrayList<Employee> getEmployeeByName(String name) throws DomainException{
    ArrayList<Employee> res = employeeRepo.getByName(name);
    //pull up more results from db.
    ArrayList<Employee> resDB = DataBaseCacher.<Employee>getByNameQuery(name,"employees",(id)->new Employee("foo",id));
    return mergeArrays(res,resDB);
  }

  // apply custom selection filter to events/employees/equipment
  @Override
  public ArrayList<Event> filterEvents(Function<Event,Boolean> filter) throws DomainException{
    return this.<Event>filterHelper("events",eventRepo,filter,(id) -> new Event(id));
  }
  @Override
  public ArrayList<Equipment> filterEquipment(Function<Equipment,Boolean> filter) throws DomainException{
    return this.<Equipment>filterHelper("equipment",equipmentRepo,filter,(id) -> new Equipment("Foo", id));
  }
  @Override
  public ArrayList<Employee> filterEmployees(Function<Employee,Boolean> filter) throws DomainException{
    return this.<Employee>filterHelper("employees",employeeRepo,filter,(id) -> new Employee("Foo", id));
  }

  private <T> ArrayList<T> filterHelper(String sLoc,RepoInterface ri, Function<T,Boolean> filter, Function<Long,T> cFunc)throws DomainException{
    ArrayList<T> res1 = ri.getByFilter(filter);

    ArrayList<T> res2 = DataBaseCacher.selectAllQuery(sLoc, cFunc);
    res2 = filterArray(res2, filter);

    return mergeArrays(res1, res2);
  }

  //return events that occure within the given range
  @Override
  public ArrayList<Event> getEventsInRange(Date start, Date end) throws DomainException{
    ArrayList<Event> events1 = eventRepo.getByDate(new DateRange(start,end));

    ArrayList<Event> events2 = DataBaseCacher.getEventsInRange(new DateRange(start,end));

    return mergeArrays(events1, events2);
  }

  // remove the given object
  @Override
  public void removeEvent(Event toRemove)throws DomainException{
    removeEventId(toRemove.getId());
  }
  @Override
  public void removeEquipment(Equipment toRemove)throws DomainException{
    equipmentRepo.remove(toRemove);
    DataBaseCacher.deleteFromDB(toRemove);
  }
  @Override
  public void removeEmployee(Employee toRemove)throws DomainException{
    employeeRepo.remove(toRemove);
    DataBaseCacher.deleteFromDB(toRemove);
  }

  //remove object with id
  @Override
  public void removeEventId(long id)throws DomainException{
    Event e = getEventById(id);
    if(e == null){
      throw new DomainException("Event does not exist!");
    }
    else{
      eventRepo.remove(id);
      DataBaseCacher.deleteFromDB(e);
    }
  }
  @Override
  public void removeEquipmentId(long id)throws DomainException{
    Equipment e = getEquipmentById(id);
    if(e == null){
      throw new DomainException("Equipment does not exist!");
    }
    else{
      equipmentRepo.remove(id);
      DataBaseCacher.deleteFromDB(e);
    }
  }
  @Override
  public void removeEmployeeId(long id)throws DomainException{
    Employee e = getEmployeeById(id);
    if(e == null){
      throw new DomainException("Employee does not exist!");
    }
    else{
      employeeRepo.remove(id);
      DataBaseCacher.deleteFromDB(e);
    }
  }

  private ArrayList mergeArrays(ArrayList a1, ArrayList a2){
    ArrayList returnList = null;
    if (a1 != null && a2 != null){
      a1.addAll(a2);
      returnList = a1;
    }
    else if(a1 != null){
      returnList = a1;
    }
    else if (a2 != null){
      returnList = a2;
    }

    //inforce null return when no results found.
    if(returnList.size() <= 0){
      return null;
    }
    else{
      return this.makeUnique(returnList);
    }
  }

  //filter array to insure every element has a unique id.
  private ArrayList makeUnique(ArrayList array){
    HashMap<Long,Object> seen = new HashMap<>();

    for(Object thing : array){
      Identifiable thingId = (Identifiable)thing;
      if(!seen.containsKey(thingId.getId())){
        seen.put(thingId.getId(),thing);
      }
    }
    return new ArrayList<Object>(seen.values());
  }

  private <T> ArrayList<T> filterArray(ArrayList<T> array, Function<T,Boolean> filter){
    if(array == null){
      return null;
    }

    ArrayList<T> acceptedElements = new ArrayList<T>();
    for(T el : array){
      if(filter.apply(el)){
        acceptedElements.add(el);
      }
    }
    return acceptedElements;
  }

}


// used to fetch information from database on "cache" lookup failure.
class DataBaseCacher {

  public static StorableInterface checkDBOnFailure(StorableInterface success, Supplier<StorableInterface> constructFunc) throws DomainException{
    if(success == null){
      //cache miss! attempt db load
      return checkDB(constructFunc);
    }
    //found in cahce just pass through
    return success;
  }

  public static StorableInterface checkDB(Supplier<StorableInterface> constructFunc) throws DomainException{
    StorableInterface storeObj = constructFunc.get();

    try{
      DefaultDBInterface dbI = DefaultDBInterface.getInstance();
      dbI.load(storeObj);
      return storeObj;
    }
    catch(StorageDriverException e){
      // this is expected to happen when no records found
      return null;
    }
    catch(JDBCException e){
      throw new DomainException("Could not perform cache lookup! with error:\n" + e.getMessage());
    }
  }

  public static <T> ArrayList<T> queryDatabase(DynamicLookupQuery<T> dlq) throws DomainException{
    try{
      DefaultDBInterface dbI = DefaultDBInterface.getInstance();
      dbI.load(dlq);
      return dlq.get();
    }
    catch(StorageDriverException e){
      // this will happen on no results
      return null;
    }
    catch(JDBCException e){
      throw new DomainException("Database query failed with error:\n" + e.getMessage());
    }
  }

  public static <T> ArrayList<T> getByNameQuery(String name, String storageLocation,
          final Function<Long,StorableInterface> cFunc) throws DomainException {
    DynamicLookupQuery<T> query = new DynamicLookupQuery<T>("id","name == \"" + name + "\"", storageLocation,
    (row) -> {
      StorableInterface newObj = cFunc.apply(((Number)row.get("id")).longValue());
      try{
        DefaultDBInterface dbI = DefaultDBInterface.getInstance();
        dbI.load(newObj);
        return (T)newObj;
      }
      catch(JDBCException|StorageDriverException e){
        //just suppress
        return null;
      }
    });
    return DataBaseCacher.<T>queryDatabase(query);
  }

  public static <T> ArrayList<T> selectAllQuery(String storageLoc, final Function<Long,T> cFunc) throws DomainException{
    DynamicLookupQuery<T> query = new DynamicLookupQuery<T>("id","",storageLoc,
    (row) -> {
      try{
        DefaultDBInterface dbI = DefaultDBInterface.getInstance();
        T obj = cFunc.apply(((Number)row.get("id")).longValue());
        dbI.load((StorableInterface)obj);
        return obj;
      }
      catch(JDBCException|StorageDriverException e){
        return null;
      }
    });

    return DataBaseCacher.<T>queryDatabase(query);
  }

  public static ArrayList<Event> getEventsInRange(DateRange dr) throws DomainException{
    try{
      DefaultDBInterface dbI = DefaultDBInterface.getInstance();
      EventRangeQuery erq = new EventRangeQuery(dr);
      dbI.load(erq);
      return erq.getEventList();
    }
    catch(StorageDriverException e){
      //prob no entires in DB
      return null;
    }
    catch(JDBCException e){
      throw new DomainException("could not look up events with error:\n" + e.getMessage());
    }
  }

  public static void deleteFromDB(StorableInterface obj) throws DomainException{
    try{
      DefaultDBInterface dbI = DefaultDBInterface.getInstance();
      dbI.delete(obj);
    }
    catch(StorageDriverException e){
      // the object was probably not stored in db.
      return;
    }
    catch(JDBCException e){
      throw new DomainException("Could not delete record from DB with error:\n" + e.getMessage());
    }
  }
}
