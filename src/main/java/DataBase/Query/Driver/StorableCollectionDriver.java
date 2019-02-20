package database.query.driver;

import java.util.ArrayList;
import java.util.HashMap;
import database.util.StorableCollection;
import database.query.*;

public class StorableCollectionDriver extends GenericStorageDriver{

  @Override
  public String toSelectSQL(StorableInterface obj) throws StorageDriverException{
    StorableCollection sc = (StorableCollection)obj;
    return "SELECT * FROM " + obj.getStorageLocation() + " WHERE id = " + sc.getId() + ";";
  }

  @Override
  public void fromSQL(ArrayList<HashMap<String, Object>> queryResults, StorableInterface obj)
      throws StorageDriverException{
      ArrayList<Object> con = new ArrayList<>();
      con.add(queryResults);
      obj.constructFromStorageValues(con);
  }
}
