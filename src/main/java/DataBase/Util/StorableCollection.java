package database.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import database.query.StorableInterface;
import database.query.driver.StorableCollectionDriver;

public class StorableCollection <T> extends ArrayList<T> implements StorableInterface{
  public StorableCollection(Collection<T> other, String storageLocation, long collectionId, int rowLength){
    super(other);
    this.storageLocation = storageLocation;
    this.collectionId = collectionId;
    this.rowLength = rowLength;
  }

  @Override
  public String getStorageDriver(){
    return "storable_collection";
  }

  @Override
  public ArrayList<Object> getStorageValues(){
    return toStorageArray();
  }

  @Override
  public void constructFromStorageValues(ArrayList<Object> vals){
    ArrayList<HashMap<String,Object>> queryResults = (ArrayList<HashMap<String,Object>>)vals.get(0);

    clear();
    String [] columnMap = getColumnMap();

    for(HashMap<String,Object> row : queryResults){
      for(String mapping : columnMap){
        add((T)row.get(mapping));
      }
    }
  }

  @Override
  public String getStorageLocation(){
    return "events_resources";
  }

  public long getId(){
    return collectionId;
  }

  //map column names to array indexes (required for loading).
  // EX: {"first_name", "last_name", "job"} means:
  // index 0 is first_name, 1 is last_name, 2 is job.
  // this pattern repeates for every row.
  public void setColumnMap(String [] map){
    columnMap = map;
  }

  public String [] getColumnMap(){
    return columnMap;
  }

  // convert array to storage format
  // insert collectionId at first index of each row
  private ArrayList<Object> toStorageArray(){
    ArrayList<Object> out = (ArrayList<Object>)this.clone();
    for(int i=0; i < out.size(); i += rowLength + 1){
      out.add(i,(Object)collectionId);
    }
    return out;
  }

  private String storageLocation;
  private long collectionId;
  private int rowLength;
  private String[] columnMap = null;
}
