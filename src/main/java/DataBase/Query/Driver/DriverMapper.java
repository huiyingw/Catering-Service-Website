package database.query.driver;

import java.util.HashMap;
import database.query.StorageDriverMap;
import database.query.StorageDriverBase;

public class DriverMapper{
  public static void populateMap(){
    HashMap<String, StorageDriverBase> map = new HashMap<>();

    // driver mappings go here
    // when you write a driver put a new mapping in this list.
    map.put("account", new AccountStorageDriver());
    map.put("employee", new EmployeeStorageDriver());
    map.put("equipment", new EquipmentStorageDriver());
    map.put("event", new EventStorageDriver());
    map.put("storable_collection", new StorableCollectionDriver());
    map.put("event_range_query", new EventRangeDriver());
    map.put("dynamic_lookup", new DynamicLookupDriver());

    StorageDriverMap.getInstance().setDriverMapping(map);
  }
}
