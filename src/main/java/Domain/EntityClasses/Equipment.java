package domain.entityclasses;

import java.util.*;

import database.query.StorableInterface;
import database.query.driver.EquipmentStorageDriver;
import domain.util.Identifiable;

public class Equipment implements AssetType, StorableInterface, Identifiable{

	public Equipment() {}

	//new Equipment object with name and quantity, none currently in use
	public Equipment(String s) {
		this.name = s;
	}

  public Equipment(String s, long id){
    this(s);
    equipmentId = id;
  }

  public void setSchedule(Schedule s){
    mySchedule = s;
  }

  public void addSchedulable(Schedulable s){
  	mySchedule.add(s);
  }
  
  public ArrayList<Schedulable> getAllScheduleItems(){
    return mySchedule != null ? mySchedule.dumpSchedule() : null;
  }

  // Identifiable Interface -----------------------
  @Override
  public long getId(){
    return equipmentId;
  }

  @Override
  public void setId(long id){
    equipmentId = id;
  }

  @Override
  public String getName(){
    return name;
  }

  @Override
  public void setName(String name){
    this.name = name;
  }
  /////////////////////////////////////////////////
  // AssetType Interface --------------------------
  @Override
  public long getAssetType(){
    return AssetTypeMap.getAssetId(assetName);
  }

  @Override
  public void setAssetId(long id){
    equipmentId = id;
  }

  @Override
  public long getAssetId(){
    return equipmentId;
  }
  /////////////////////////////////////////////////
  // StorableInterface -----------------------------
  @Override
  public String getStorageDriver(){
    return "equipment";
  }
  @Override
  public ArrayList<Object> getStorageValues(){
    ArrayList<Object> vals = new ArrayList<>();
    vals.add((Object)equipmentId);
    vals.add((Object)AssetTypeMap.getAssetId(assetName));
    vals.add((Object)0l);
    vals.add((Object)name);
    return vals;
  }

  @Override
  public void constructFromStorageValues(ArrayList<Object> vals){
    setName((String)vals.get(0));
  }

  @Override
  public String getStorageLocation(){
    return "equipment";
  }
  ////////////////////////////////////////////////////

  private String name;
  private String assetName = "null";
  private long equipmentId;
  private Schedule mySchedule = null;
}
