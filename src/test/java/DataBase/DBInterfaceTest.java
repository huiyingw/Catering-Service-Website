import org.junit.*;

import database.api.DebugDBInterface;
import database.api.DBInterface;
import database.query.StorableInterface;
import database.query.StorageDriverBase;
import database.query.StorageDriverException;
import database.jdbc.JDBCException;
import database.jdbc.JDBCConnectionType;

import domain.entityclasses.Employee;
import domain.entityclasses.Equipment;
import domain.entityclasses.Event;
import domain.entityclasses.AssetType;
import domain.util.EventRangeQuery;
import domain.util.DateRange;
import web.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

public class DBInterfaceTest{

  public DBInterfaceTest(){
    //nop
  }

  @Test
  public void storeLoadTest()throws Exception {
    DebugDBInterface dbI = DebugDBInterface.getInstance();
    dbI.ensureConnection(JDBCConnectionType.SQLITE);
    dbI.customInitializeDB((q) -> {
      try{
        q.query("DROP TABLE IF EXISTS foobar;");
        q.query("CREATE TABLE IF NOT EXISTS foobar (id INTEGER, x INTEGER);");
      }
      catch(JDBCException e){
        // cannot throw exceptions from lambdas :(
        // why java... why you do this!!!!!!
        assert(false);
      }
    });
    dbI.registerStorageDriver("thing123", new ThingStorageDriver());

    Thing thing1 = new Thing();
    thing1.id = 420;
    thing1.x = 42;
    dbI.store(thing1);

    Thing thing2 = new Thing();
    thing2.id = 420;
    dbI.load(thing2);

    assert(thing2.x.equals(thing1.x));
  }

  @Test
  public void employeeDB()throws Exception {
    DebugDBInterface dbI = DebugDBInterface.getInstance();
    dbI.ensureConnection(JDBCConnectionType.SQLITE);
    dbI.NUKE();
    dbI.initializeDB();
    Employee frank = new Employee();
    frank.setName("Frank");
    frank.setId(1234);

    dbI.store(frank);
    frank.setName("NotFrank");

    dbI.load(frank);
    assert(frank.getName().equals("Frank"));
    assert(dbI.getNextIdForTable("employees") == 1235);

    deleteObjTest(frank,dbI);
  }

  @Test
  public void equipmentDB()throws Exception {
    DebugDBInterface dbI = DebugDBInterface.getInstance();
    dbI.ensureConnection(JDBCConnectionType.SQLITE);
    dbI.NUKE();
    dbI.initializeDB();
    Equipment food = new Equipment();
    food.setName("Burger");
    food.setId(2894);

    dbI.store(food);
    food.setName("Spinich");

    dbI.load(food);
    assert(food.getName().equals("Burger"));

    deleteObjTest(food,dbI);
  }

  @Test
  public void eventDB()throws Exception {
    DebugDBInterface dbI = DebugDBInterface.getInstance();
    dbI.ensureConnection(JDBCConnectionType.SQLITE);
    dbI.NUKE();
    dbI.initializeDB();

    //setup event
    Event event = new Event();
    event.setName("Party_My_House");
    event.setId(1337);
    Date startTime = new Date(2017 - 1900, 10,25,0,0,0);
    event.setStartDate(startTime);
    event.setEndDate(new Date(2017 - 1900, 10,26,0,0,0));

    Employee bob = new Employee();
    bob.setId(789);
    bob.setName("Bob");
    ArrayList<AssetType> emps = new ArrayList<>();
    emps.add(bob);
    event.setAssigned(emps);

    //store
    dbI.store(bob);
    dbI.store(event);

    // clear set values
    event.setName("no party");
    event.setAssigned(null);
    event.setStartDate(null);
    event.setEndDate(null);

    //load
    dbI.load(event);

    //check values
    assert(event.getName().equals("Party_My_House"));
    assert(event.getAssigned().get(0).equals(bob));
    assert(((Employee)event.getAssigned().get(0)).getName().equals(bob.getName()));
    assert(event.getDateRange().getStart().getTime() == startTime.getTime());
    assert(event.getDateRange().getEnd() != null);

    deleteObjTest(event,dbI);
  }

  @Test
  public void accountDB()throws Exception {
    DebugDBInterface dbI = DebugDBInterface.getInstance();
    dbI.ensureConnection(JDBCConnectionType.SQLITE);
    dbI.NUKE();
    dbI.initializeDB();
    Account acc = new Account();
    acc.setuserID("bill");
    acc.setuserType(1);
    acc.setPassword("WIN");
    acc.setToken("123456");

    dbI.store(acc);
    acc.setPassword("FAIL");
    acc.setuserType(2);
    acc.setToken("1232131243424");

    dbI.load(acc);
    assert(acc.getPassword().equals("WIN"));
    assert(acc.getuserType() == 1);
    assert(acc.getToken().equals("123456"));

    deleteObjTest(acc,dbI);
  }

  @Test
  public void eventRange()throws Exception {
    DebugDBInterface dbI = DebugDBInterface.getInstance();
    dbI.ensureConnection(JDBCConnectionType.SQLITE);
    dbI.NUKE();
    dbI.initializeDB();

    //setup event
    Event event1 = new Event();
    event1.setName("Party_My_House");
    event1.setId(1337);
    Date startTime = new Date(2017 - 1900, 10,25,0,0,0);
    event1.setStartDate(startTime);
    event1.setEndDate(new Date(2017 - 1900, 10,26,0,0,0));

    Event event2 = new Event();
    event2.setName("Party_Your_House");
    event2.setId(214124);
    startTime = new Date(2017 - 1900, 10,28,0,0,0);
    event2.setStartDate(startTime);
    event2.setEndDate(new Date(2017 - 1900, 10,29,0,0,0));

    //store
    dbI.store(event1);
    dbI.store(event2);

    //range query
    DateRange dr = new DateRange(new Date(2017 - 1900, 10,25,0,0,0), new Date(2017 - 1900, 10,27,0,0,0));
    EventRangeQuery erq = new EventRangeQuery(dr);
    dbI.load(erq);

    ArrayList<Event> results = erq.getEventList();
    assert(results.size() == 1);
    assert(results.get(0).getId() == event1.getId());
  }

  @Test
  public void updateRecordTest() throws Exception{
    DebugDBInterface dbI = DebugDBInterface.getInstance();
    dbI.ensureConnection(JDBCConnectionType.SQLITE);
    dbI.NUKE();
    dbI.initializeDB();

    Account acc = new Account();
    acc.setuserID("bill");
    acc.setuserType(1);
    acc.setPassword("WIN");
    acc.setToken("1234556");

    Account acc2 = new Account();
    acc2.setuserID("Frank");
    acc2.setuserType(1);
    acc2.setPassword("WIN");
    acc2.setToken("123244");

    dbI.store(acc);
    dbI.store(acc2);
    acc.setPassword("newPassword");
    dbI.store(acc);
    acc.setPassword("FAIL");
    acc2.setPassword("FAIL");

    dbI.load(acc);
    dbI.load(acc2);
    assert(acc.getPassword().equals("newPassword"));
    assert(acc2.getPassword().equals("WIN"));
  }


  private void deleteObjTest(StorableInterface obj, DBInterface dbI) throws Exception{
    //delete
    dbI.delete(obj);
    boolean success = false;
    try{
      dbI.load(obj);
    }
    catch(StorageDriverException e){
      //should happen if the record does not exist in db.
      success = true;
    }
    assert(success);
  }
}

// thing to store
class Thing implements StorableInterface{

  @Override
  public String getStorageDriver(){
    return "thing123";
  }

  @Override
  public ArrayList<Object> getStorageValues(){
    ArrayList<Object> res = new ArrayList<>();
    res.add(id);
    res.add(x);
    return res;
  }

  @Override
  public void constructFromStorageValues(ArrayList<Object> vals){

  }

  @Override
  public String getStorageLocation(){
    return "foobar";
  }
  public Integer id = 42;
  public Integer x = 3;
}

// storage driver for Thing object
class ThingStorageDriver extends StorageDriverBase{

  @Override
  public String toInsertSQL(StorableInterface obj) throws StorageDriverException{
    Thing th = (Thing) obj;
    return "INSERT INTO " + obj.getStorageLocation() + " VALUES (" + th.id + "," + th.x + ");";
  }

  @Override
  public String toReplaceSQL(StorableInterface obj) throws StorageDriverException {
    throw new StorageDriverException("Not Implemented");
  }

  @Override
  public String toSelectSQL(StorableInterface obj) throws StorageDriverException{
    Thing th = (Thing) obj;
    return "SELECT * FROM " + obj.getStorageLocation() + " WHERE id = " + th.id + ";";
  }

  @Override
  public String toDeleteSQL(StorableInterface obj) throws StorageDriverException{
    return "FOOBAR";
  }

  @Override
  public void fromSQL(ArrayList<HashMap<String, Object>> queryResults, StorableInterface obj)
      throws StorageDriverException{
    Thing th = (Thing) obj;
    th.id = (Integer)queryResults.get(0).get("id");
    th.x = (Integer)queryResults.get(0).get("x");
  }
}
