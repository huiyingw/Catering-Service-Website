package database.query;

import java.util.HashMap;

public class StorageDriverMap{

  private static StorageDriverMap driverMap = new StorageDriverMap();
  public static StorageDriverMap getInstance(){
    return driverMap;
  }

  private StorageDriverMap(){}

  private StorageDriverMap(HashMap<String, StorageDriverBase> driverMapping){
    setDriverMapping(driverMapping);
  }

  public void setDriverMapping(HashMap<String, StorageDriverBase> driverMapping){
    sMap = driverMapping;
  }

  public void appendDriverMapping(String name, StorageDriverBase driver){
    sMap.put(name, driver);
  }

  public void removeDriverMapping(String name){
    sMap.remove(name);
  }

  public StorageDriverBase getStorageDriver(String name){
    return sMap.get(name);
  }

  private HashMap<String, StorageDriverBase> sMap = new HashMap<>();
}
