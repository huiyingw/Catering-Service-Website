package database.query.driver;

import java.util.ArrayList;
import java.util.HashMap;

import database.query.*;
import database.jdbc.JDBCException;
import database.api.DefaultDBInterface;
import domain.entityclasses.AssetType;
import domain.util.Identifiable;
import database.util.StorableCollection;

public class EventStorageDriver extends StorageDriverBase{

  // convert storable object to insertion sql code
  @Override
  public String toInsertSQL(StorableInterface obj) throws StorageDriverException{
    ArrayList<Object> vals = obj.getStorageValues();

    //store assigned assets
    ArrayList<AssetType> assets = (ArrayList<AssetType>)vals.get(4);
    try{
      storeAssetsArray(assets,(Long)vals.get(0),obj.getStorageLocation()+"_resources");
    }
    catch(JDBCException e){
      throw new StorageDriverException("Could not store assets assigned to this event: " + e);
    }

    String rSQL = "INSERT INTO " + obj.getStorageLocation() + " VALUES (" + arrayListToCSV(vals.subList(0,vals.size()-1)) + ");";
    return rSQL;
  }

  @Override
  public String toReplaceSQL(StorableInterface obj) throws StorageDriverException {
    String sql = toInsertSQL(obj);
    return sql.replaceAll("^INSERT\\b", "REPLACE");
  }

  // convert storable object to selection SQL (as to load object from database).
  @Override
  public String toSelectSQL(StorableInterface obj) throws StorageDriverException{
    ArrayList<Object> valsToStore = obj.getStorageValues();
    return "SELECT * FROM " + obj.getStorageLocation() + " WHERE id = " + valsToStore.get(0) + ";";
  }

  @Override
  public String toDeleteSQL(StorableInterface obj) throws StorageDriverException{
    //delete asset mappings
    StorableCollection<Number> sc = new StorableCollection<Number>(new ArrayList<Number>()
        ,obj.getStorageLocation()+"_resources", (Long)obj.getStorageValues().get(0) ,2);
    String [] mapping = {"resource_id","asset_type"};
    sc.setColumnMap(mapping);

    try{
      DefaultDBInterface dbI = DefaultDBInterface.getInstance();
      dbI.delete(sc);
    }
    catch(JDBCException e){
      throw new StorageDriverException("Could not delete event asset rows with error:\n" + e.getMessage());
    }

    String selSQL = toSelectSQL(obj);
    return selSQL.replaceAll("^SELECT[\\w\\s,\\*]+FROM\\b","DELETE FROM");
  }

  // initilize object from SQL results
  @Override
  public void fromSQL(ArrayList<HashMap<String, Object>> queryResults, StorableInterface obj)
      throws StorageDriverException{
      HashMap<String,Object> row1 = queryResults.get(0);
      ArrayList<Object> constructList = new ArrayList<>();

      constructList.add(row1.get("name"));
      constructList.add(row1.get("start_time"));
      constructList.add(row1.get("end_time"));
      StorableCollection<Number> sc = new StorableCollection<Number>(new ArrayList<Number>()
          ,obj.getStorageLocation()+"_resources", (Long)obj.getStorageValues().get(0) ,2);
      String [] mapping = {"resource_id","asset_type"};
      sc.setColumnMap(mapping);
      constructList.add(sc);

      obj.constructFromStorageValues(constructList);
  }

  // store an array of Assets in to the DB.
  private void storeAssetsArray(ArrayList<AssetType> ass, long event_id, String storage_location) throws JDBCException, StorageDriverException{
    if(ass != null){
      ArrayList<Long> assetIds = new ArrayList<>();
      for( AssetType at : ass){
        assetIds.add(((Identifiable)at).getId());
        assetIds.add(at.getAssetType());
      }

      StorableCollection<Long> aStore = new
          StorableCollection<Long>(assetIds, storage_location, event_id, 2);
      DefaultDBInterface.getInstance().store(aStore);
    }
  }
}
