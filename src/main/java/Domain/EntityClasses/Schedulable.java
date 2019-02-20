package domain.entityclasses;

import java.util.ArrayList;
import java.util.Date;
import domain.util.DateRange;

// implemented by objects that can be stored in a schedule
public interface Schedulable {

  // return name of the schedulable object
  public abstract String getName();

  // return object id
  public abstract long getId();

  // return the type of the schedulable object
  public abstract SchedulableType getType();

  // return a list of asset type ids. a schedulable object
  // is considered astisfied if there exists an object in the system,
  // to map to every id in this list;
  public abstract ArrayList<Long> getRequirements();
  public abstract void setRequirements(ArrayList<Long> req);

  //set / get assigned resources for this event.
  public abstract void setAssigned(ArrayList<AssetType> assigned);
  public abstract ArrayList<AssetType> getAssigned();

  public abstract DateRange getDateRange();
  public abstract void setDateRange(DateRange dr);
}
