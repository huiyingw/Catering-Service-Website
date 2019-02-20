package domain.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import database.query.StorableInterface;
import database.api.DefaultDBInterface;
import database.query.StorageDriverException;
import database.jdbc.JDBCException;

public class DynamicLookupQuery<T> implements StorableInterface {

  public DynamicLookupQuery(String selectSQL, String conditionSQL,String table,
                                  Function<HashMap<String,Object>, T> conFunc){
    this.selectSQL = selectSQL;
    this.conditionSQL = conditionSQL;
    this.constructFunc = conFunc;
    tableSQL = table;
  }

  public ArrayList<T> get(){
    return results;
  }

  // StorableInterface -------------------------------------
  public String getStorageDriver(){
    return "dynamic_lookup";
  }

  public ArrayList<Object> getStorageValues(){
    ArrayList<Object> vals = new ArrayList<>();
    vals.add(selectSQL);
    vals.add(conditionSQL);
    return vals;
  }

  public void constructFromStorageValues(ArrayList<Object> vals){
    // each element is a hashmap of one row.
    for(Object rowH : vals){
      T res = constructFunc.apply((HashMap<String,Object>)rowH);
      if(res != null){
        results.add(res);
      }
    }
  }

  public String getStorageLocation(){
    return tableSQL;
  }
  /////////////////////////////////////////////////////////

  private String tableSQL;
  private String selectSQL;
  private String conditionSQL;
  private ArrayList<T> results = new ArrayList<>();
  private Function<HashMap<String,Object>, T> constructFunc;
}
