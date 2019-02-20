package domain.api.repo;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.function.Function;
import java.lang.UnsupportedOperationException;

import domain.entityclasses.AssetType;
import domain.util.AdvancedCast;
import domain.util.Identifiable;

// not T must be implement Identifiable. well maby not, perhaps this class
// should be refactored !!!!!!!!!!!!!
public class IdentifiableRepo<T> implements RepoInterface<T>{
  // get object by id
  @Override
  public T getById(long id){
    return (T)assetHash.get(id);
  }

  // returns the FIRST object with name
  @Override
  public ArrayList<T> getByName(String name){
    return getByFilter((x) -> ((Identifiable)x).getName().equals(name));
  }

  // return a list of objects that where accepted by filer function.
  @Override
  public ArrayList<T> getByFilter(Function<T,Boolean> filter){
    ArrayList<T> result = new ArrayList<>();
    for(Identifiable equ : assetHash.values()){
      if(filter.apply((T)equ)){
        result.add((T)equ);
      }
    }
    return result;
  }

  // add object obj to repo.
  @Override
  public void add(T obj){
    assetHash.put(((Identifiable)obj).getId(),(Identifiable)obj);
  }

  // remove object matching obj from repo
  @Override
  public void remove(T obj){
    assetHash.remove(((Identifiable)obj).getId(),(Identifiable)obj);
  }

  // remove object from repo with matching id
  @Override
  public void remove(long id){
    assetHash.remove(id);
  }

  // count the number of objects stored in this repo
  @Override
  public int size(){
    return assetHash.size();
  }

  // flush this repo to disk i.e. store all contents in the DB.
  @Override
  public void flushToDisk(){

  }

  //to be used by subclasses to get access to the hash map in a format they can understand.
  protected <T> HashMap<Long, T> getHashMapAs(){
    return AdvancedCast.<Long, Identifiable, Long, T>cast(assetHash);
  }

  private HashMap<Long, Identifiable> assetHash = new HashMap<>();
}
