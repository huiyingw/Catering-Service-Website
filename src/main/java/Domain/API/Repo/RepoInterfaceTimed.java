package domain.api.repo;

import java.util.ArrayList;

import domain.util.DateRange;

public interface RepoInterfaceTimed<T> extends RepoInterface<T>{

  //return list of objects within the given date range.
  public abstract ArrayList<T> getByDate(DateRange dr);

}
