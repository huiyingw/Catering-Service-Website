package database.query.driver;

import java.util.ArrayList;
import java.util.HashMap;

import database.query.StorageDriverBase;
import database.query.StorageDriverException;
import database.query.StorableInterface;

class EventRangeDriver extends StorageDriverBase{
  // convert storable object to insertion sql code
  public String toInsertSQL(StorableInterface obj) throws StorageDriverException{
    throw new StorageDriverException("EventRangeDriver cannot store values");
  }

  // convert storable object to update sql code
  public String toReplaceSQL(StorableInterface obj) throws StorageDriverException{
    throw new StorageDriverException("EventRangeDriver cannot store values");
  }

  // convert storable object to selection SQL (as to load object from database).
  public String toSelectSQL(StorableInterface obj) throws StorageDriverException{
    ArrayList<Object> vals = obj.getStorageValues();
    return "SELECT id FROM " + obj.getStorageLocation() +
        " WHERE start_time BETWEEN \"" + vals.get(0) + "\" AND \"" + vals.get(1) + "\" ;";
  }

  @Override
  public String toDeleteSQL(StorableInterface obj) throws StorageDriverException{
    throw new StorageDriverException("Event Range Cannot delete objects");
  }

  // initilize object from SQL results
  public void fromSQL(ArrayList<HashMap<String, Object>> queryResults, StorableInterface obj)
      throws StorageDriverException{
    ArrayList<Object> idList = new ArrayList<>();

    for(HashMap<String,Object> row : queryResults){
      idList.add(row.get("id"));
    }

    obj.constructFromStorageValues(idList);
  }
}
