package database.query;

import java.util.ArrayList;
import java.util.HashMap;

// WAR! generic storage driver assumes that the first storage value is an id.
public class GenericStorageDriver extends StorageDriverBase{

    // dumb convert of storage values to sql insert statment.
    @Override
    public String toInsertSQL(StorableInterface obj) throws StorageDriverException{
      ArrayList<Object> values = obj.getStorageValues();
      String sqlQuery = "INSERT INTO " + obj.getStorageLocation() + " VALUES (";
      sqlQuery += arrayListToCSV(values);
      return sqlQuery + ");";
    }

    @Override
    public String toReplaceSQL(StorableInterface obj) throws StorageDriverException {
      String sql = toInsertSQL(obj);
      return sql.replaceAll("^INSERT\\b","REPLACE");
    }

    @Override
    public String toDeleteSQL(StorableInterface obj) throws StorageDriverException{
      String selSQL = toSelectSQL(obj);
      return selSQL.replaceAll("^SELECT[\\w\\s,\\*]+FROM\\b","DELETE FROM");
    }

    // NOT IMPLEMENTED
    @Override
    public String toSelectSQL(StorableInterface obj) throws StorageDriverException{
      throw new StorageDriverException("Generic Storage Driver cannot convert storable to selection SQL");
    }

    // NOT IMPLEMENTED
    @Override
    public void fromSQL(ArrayList<HashMap<String, Object>> queryResults, StorableInterface obj)
        throws StorageDriverException{
      throw new StorageDriverException("Generic Storage Driver cannot load from SQL");
    }
}
