package domain.entityclasses;

import java.util.Date;
import java.util.ArrayList;
import java.text.ParseException;

import domain.util.DateRange;
import domain.util.ISODate;
import domain.util.Identifiable;
import database.util.StorableCollection;
import database.api.DefaultDBInterface;
import database.query.StorableInterface;
import database.jdbc.JDBCException;
import database.query.StorageDriverException;

public class Event implements Schedulable, StorableInterface, Identifiable{

	//empty Event object
	public Event() {}

  public Event(long id){
    setId(id);
  }

	//Event object with specified parameters
	public Event(String s, Date start, Date end) {
		this.name = s;
		dateRange = new DateRange(start, end);
	}

	public void setStartDate(Date start) {
		dateRange = dateRange == null ?
        new DateRange(start,new Date()) : new DateRange(start, dateRange.getEnd());
	}

	public void setEndDate(Date end) {
    dateRange = dateRange == null ?
        new DateRange(new Date(), end) : new DateRange(dateRange.getStart(),end);
	}

  public EventDetails getEventDetails(){
    return eventInfo;
  }

  public void setEventDetails(EventDetails eventD){
    eventInfo = eventD;
  }

  public CustomerInfo getCustomerInfo(){
    return customerInformation;
  }

  public void setCustomerInfo(CustomerInfo customerI){
    customerInformation = customerI;
  }
  //setter and getters

  public Date getStartDate(){
    return dateRange.getStart();
  }

  public Date getEndDate(){
    return dateRange.getEnd();
  }

  //Identifiable interface -----------------
  @Override
  public void setId(long id){
    eventId = id;
  }
  @Override
  public void setName(String name){
    this.name = name;
  }
  //////////////////////////////////////////
  // Schedulable interface -----------------
  @Override
  public String getName(){
    return name;
  }

  @Override
  public long getId(){
    return eventId;
  }

  @Override
  public SchedulableType getType(){
    return SchedulableType.SCHED_EVENT;
  }

  @Override
  public ArrayList<Long> getRequirements(){
    return requiredAssets;
  }

  @Override
  public void setRequirements(ArrayList<Long> req){
     requiredAssets = req;
  }

  @Override
  public void setAssigned(ArrayList<AssetType> assigned){
    assignedResources = assigned;
  }

  @Override
  public ArrayList<AssetType> getAssigned(){
    return assignedResources;
  }

  @Override
  public DateRange getDateRange(){
    return dateRange;
  }

  @Override
  public void setDateRange(DateRange dr){
    dateRange = dr;
  }
  //////////////////////////////////////////////////
  // StorableInterface -----------------------------
  @Override
  public String getStorageDriver(){
    return "event";
  }

  @Override
  public ArrayList<Object> getStorageValues(){
    ArrayList<Object> vals = new ArrayList<>();

    //convert dates to ISO8601 format
    ISODate start = new ISODate();
    ISODate end = new ISODate();
    start.fromDate(dateRange.getStart());
    end.fromDate(dateRange.getEnd());

    vals.add((Object)eventId);
    vals.add((Object)start);
    vals.add((Object)end);
    vals.add((Object)name);
    vals.add((Object)assignedResources);
    return vals;
  }

  @Override
  public void constructFromStorageValues(ArrayList<Object> vals){

    setName((String)vals.get(0));
    try{
      ISODate start = new ISODate(vals.get(1).toString());
      ISODate end = new ISODate(vals.get(2).toString());
      setStartDate(start.toDate());
      setEndDate(end.toDate());
    }
    catch(ParseException e){
      //set some sort of resonable default
      setStartDate(new Date());
      setEndDate(new Date());
    }

    ArrayList<AssetType> assets = loadAssetsArray((StorableCollection)vals.get(3));
    if(assets != null){
      setAssigned(assets);
    }
  }

  @Override
  public String getStorageLocation(){
    return "events";
  }
  ////////////////////////////////////////////////////

  private ArrayList<AssetType> loadAssetsArray(StorableCollection<Number> sc){
    ArrayList<AssetType> assets = new ArrayList<>();

    try{
      DefaultDBInterface dbI = DefaultDBInterface.getInstance();
      dbI.load(sc);

      for(int i =0;i < sc.size();i +=2){

        long assetType = sc.get(i+1).longValue();
        long objectId = sc.get(i).longValue();

        if(assetType == AssetTypeMap.getAssetId("Employee")){
          //employee asset
          Employee tmpEmployee = new Employee("??",objectId);
          dbI.load(tmpEmployee);
          assets.add((AssetType)tmpEmployee);
        }
        else{
          //equipment of some sort.
          Equipment tmpEquip = new Equipment();
          tmpEquip.setId(objectId);
          dbI.load(tmpEquip);
          assets.add((AssetType)tmpEquip);
        }
      }
      return assets;
    }
    catch(JDBCException|StorageDriverException e){
      return null;
    }
  }

  @Override
  public String toString(){
    String desc = "location: " + eventInfo.location +  " from " + dateRange.getStart() + " to " + dateRange.getEnd();
    desc += "\nTheme: " + eventInfo.themeName;
    desc += "\nAttendance: " + eventInfo.numGuests;
    desc += "\nCustomer Instructions: " + eventInfo.customerMessage;
    return desc;
  }

  private String name;
  private DateRange dateRange = new DateRange();
  private ArrayList<AssetType> assignedResources = null;
  private ArrayList<Long> requiredAssets = new ArrayList<>();
  private long eventId;

  private CustomerInfo customerInformation = new CustomerInfo();
  private EventDetails eventInfo = new EventDetails();
}
