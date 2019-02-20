package domain.entityclasses;

import java.util.HashMap;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.lang.Long;

import domain.util.DateRange;

public class Schedule implements Iterable<Schedulable>{

  public Schedule(){}

  // initialize internal hashmap with provided hash map
  public Schedule(HashMap<Long, Schedulable> initHash){
    scheduleHash = initHash;
  }

  public Schedulable getById(long id){
    return scheduleHash.get(id);
  }

  public ArrayList<Schedulable> getByName(String name){
    ArrayList<Schedulable> out = new ArrayList<>();
    for(Schedulable s : scheduleHash.values()){
      if(s.getName().equals(name)){
        out.add(s);
      }
    }
    return out;
  }

  public ArrayList<Schedulable> getInRange(DateRange dr){
    ArrayList<Schedulable> inRange = new ArrayList<>();
    for(Schedulable s : scheduleHash.values()){
      if(dr.contains(s.getDateRange())){
        inRange.add(s);
      }
    }
    return inRange;
  }

  // returns and ArrayList of all events in this schedule
  public ArrayList<Schedulable> dumpSchedule(){
    return new ArrayList<Schedulable>(scheduleHash.values());
  }

  public void add(Schedulable s){
    scheduleHash.put(s.getId(),s);
  }

  public void remove(Long id){
    scheduleHash.remove(id);
  }

  public void remove(Schedulable s){
    scheduleHash.remove(s.getId());
  }

  public int size(){
    return scheduleHash.size();
  }

  // Iterable interface ------------------------------
  @Override
  public Iterator<Schedulable> iterator(){
    return scheduleHash.values().iterator();
  }
  ////////////////////////////////////////////////////

  private HashMap<Long, Schedulable> scheduleHash = new HashMap<Long, Schedulable>();
}
