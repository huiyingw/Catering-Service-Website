package domain.entityclasses;

import java.util.ArrayList;
import domain.util.DateRange;

import database.query.StorableInterface;
import database.query.driver.EmployeeStorageDriver;
import domain.util.Identifiable;

public class Employee implements AssetType, StorableInterface, Identifiable{

  public Employee(){}

  //constructor with name and ID
  public Employee(String s, long id){
    this.name = s;
    this.employeeId = id;
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
    return employeeId;
  }

  @Override
  public void setId(long id){
    employeeId = id;
  }

  @Override
  public String getName(){
    return this.name;
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
    employeeId = id;
  }

  @Override
  public long getAssetId(){
    return employeeId;
  }
  // ///////////////////////////////////////////////
  // StorableInterface -----------------------------
  @Override
  public String getStorageDriver(){
    return "employee";
  }

  @Override
  public ArrayList<Object> getStorageValues(){
    ArrayList<Object> vals = new ArrayList<>();
    vals.add((Object)employeeId);
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
    return "employees";
  }
  ////////////////////////////////////////////////////

  private String name;
  private String assetName = "Employee";
  private long employeeId;
  private Schedule mySchedule = null;
}
