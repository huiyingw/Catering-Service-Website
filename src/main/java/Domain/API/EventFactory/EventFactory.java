package domain.api.eventfactory;

import java.util.ArrayList;
import java.util.HashMap;

import domain.entityclasses.AssetTypeMap;
import domain.entityclasses.Event;
import domain.api.DomainException;
import domain.api.Domain;
import database.api.DefaultDBInterface;
import database.jdbc.JDBCException;
import database.query.StorageDriverException;

public class EventFactory{

  public static Event createNewEvent(EventInfo eInfoStruct) throws DomainException{
    Event newE = new Event();

    newE.setName(eInfoStruct.eventName);
    newE.setStartDate(eInfoStruct.eventStart);
    newE.setEndDate(eInfoStruct.eventEnd);
    newE.setCustomerInfo(eInfoStruct.customerInfo);
    newE.setEventDetails(eInfoStruct.eventDetails);
    newE.setRequirements(themeRequirements.get(eInfoStruct.eventDetails.themeName));

    try{
      DefaultDBInterface dbI = DefaultDBInterface.getInstance();
      newE.setId(dbI.getNextIdForTable("events"));
      dbI.store(newE);
    }
    catch(JDBCException|StorageDriverException e){
      throw new DomainException("Could not create new event object with error\n"+e.getMessage());
    }

    //store in repo
    Domain domainI = Domain.getInstance();
    domainI.addEvent(newE);

    return newE;
  }


  private static final HashMap<String,ArrayList<Long>> themeRequirements;
  static {
    themeRequirements = new HashMap<String,ArrayList<Long>>();
    themeRequirements.put("BBQ",getBBQRequirements());
    themeRequirements.put("Italian",getItalianRequirements());
    themeRequirements.put("Seafood",getSeaFoodRequirements());
    themeRequirements.put("Vegan",getVeganRequirements());
  }

  // move to database load in future -----------------------
  private static ArrayList<Long> getBBQRequirements(){
    ArrayList<Long> reqBBQ = new ArrayList<>();
    reqBBQ.add(AssetTypeMap.getAssetId("Employee"));
    return reqBBQ;
  }

  private static ArrayList<Long> getItalianRequirements(){
    ArrayList<Long> reqItalian = new ArrayList<>();
    reqItalian.add(AssetTypeMap.getAssetId("Employee"));
    return reqItalian;
  }

  private static ArrayList<Long> getSeaFoodRequirements(){
    ArrayList<Long> reqSea = new ArrayList<>();
    reqSea.add(AssetTypeMap.getAssetId("Employee"));
    return reqSea;
  }

  private static ArrayList<Long> getVeganRequirements(){
    ArrayList<Long> reqVegan = new ArrayList<>();
    reqVegan.add(AssetTypeMap.getAssetId("Employee"));
    return reqVegan;
  }
  //////////////////////////////////////////////////////////
}
