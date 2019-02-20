package domain.api.repo;

import java.util.function.Function;
import java.util.ArrayList;

public interface RepoInterface<T> {

  // get object by id
  public abstract T getById(long id);

  // returns the FIRST object with name
  public abstract ArrayList<T> getByName(String name);

  // return a list of objects that where accepted by filer function.
  public abstract ArrayList<T> getByFilter(Function<T,Boolean> filter);

  // add object obj to repo.
  public abstract void add(T obj);

  // remove object matching obj from repo
  public abstract void remove(T obj);

  // remove object from repo with matching id
  public abstract void remove(long id);

  // count the number of objects stored in this repo
  public abstract int size();

  // flush this repo to disk i.e. store all contents in the DB.
  public abstract void flushToDisk();
}
