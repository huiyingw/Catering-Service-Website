package database.query.driver;

import java.util.ArrayList;
import java.util.HashMap;
import database.query.*;

public class DynamicLookupDriver extends StorageDriverBase{
  // convert storable object to insertion sql code
  public String toInsertSQL(StorableInterface obj) throws StorageDriverException{
    throw new StorageDriverException("Dynamic Lookup Driver does not support storage");
  }

  // convert storable object to update sql code
  public String toReplaceSQL(StorableInterface obj) throws StorageDriverException{
    throw new StorageDriverException("Dynamic Lookup Driver does not support storage");
  }

  // convert storable object to selection SQL (as to load object from database).
  public String toSelectSQL(StorableInterface obj) throws StorageDriverException{
    ArrayList<Object> selectionParams = obj.getStorageValues();
    String selectSQL    = (String)selectionParams.get(0);
    String conditionSQL = (String)selectionParams.get(1);
    if(conditionSQL.isEmpty()){
      return "SELECT " + selectSQL +" FROM " + obj.getStorageLocation() + ";";
    }
    else{
      return "SELECT " + selectSQL +" FROM " + obj.getStorageLocation() + " WHERE " + conditionSQL + ";";
    }
  }

  @Override
  public String toDeleteSQL(StorableInterface obj) throws StorageDriverException{
    throw new StorageDriverException("Dynamic >>> LOOKUP <<< driver cannot delete database resorces.");
  }

  // initilize object from SQL results
  public void fromSQL(ArrayList<HashMap<String, Object>> queryResults, StorableInterface obj)
      throws StorageDriverException{
      ArrayList<Object> constructList = new ArrayList<>();

      for(HashMap<String, Object> row : queryResults){
        constructList.add((Object)row);
      }
      obj.constructFromStorageValues(constructList);
  }
}
