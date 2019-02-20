package database.query.driver;

import java.util.ArrayList;
import java.util.HashMap;
import database.query.*;

public class AccountStorageDriver extends GenericStorageDriver{

  // convert storable object to selection SQL (as to load object from database).
  public String toSelectSQL(StorableInterface obj) throws StorageDriverException{
    ArrayList<Object> valsToStore = obj.getStorageValues();
    return "SELECT * FROM " + obj.getStorageLocation() + " WHERE id = \"" + valsToStore.get(0) + "\";";
  }
  
  // initilize object from SQL results
  public void fromSQL(ArrayList<HashMap<String, Object>> queryResults, StorableInterface obj)
      throws StorageDriverException{
      HashMap<String,Object> row1 = queryResults.get(0);
      ArrayList<Object> constructList = new ArrayList<>();
      constructList.add(row1.get("password"));
      constructList.add(row1.get("user_type"));
      constructList.add(row1.get("token"));

      obj.constructFromStorageValues(constructList);
  }
}
