package database.api;

import java.util.function.Consumer;
import database.query.StorableInterface;
import database.query.StorageDriverBase;
import database.query.StorageDriverException;
import database.jdbc.JDBCConnectionManager;
import database.jdbc.JDBCException;
import database.jdbc.JDBCConnectionType;

// interface used to interact with the data base.
public interface DBInterface {

  // store the passed object to the data base (see StorableInterface for more info)
  public abstract void store(StorableInterface objToStore) throws JDBCException, StorageDriverException;

  // load the passed object from the data base. (see StorableInterface for more info)
  public abstract void load(StorableInterface objToLoad) throws JDBCException, StorageDriverException;

  // delete records of this object from the database.
  public abstract void delete(StorableInterface objToDelete) throws JDBCException, StorageDriverException;

  // returns the next valid id for which a record may be inserted in to the given table
  // note. will fail if table identified by tableName does not have a 'id' column.
  public abstract long getNextIdForTable(String tableName) throws JDBCException;

  // register a new storage driver with name. objects can then request this
  // driver (by name) when storing / loading
  public abstract void registerStorageDriver(String name, StorageDriverBase driver);

  // check the DB connection. if it is not valid attempt reconect to specified type
  public abstract void ensureConnection(JDBCConnectionType conType) throws JDBCException;

  // accepts a lambda function that will be passed the JDBCConnectionManager object
  // this lambda can do what ever it wants but, it is expected that it perform DB specific
  // initialization functions such as creating DATABASEs / TABLEs .... etc.
  public abstract void customInitializeDB(Consumer<JDBCConnectionManager> initFunction) throws JDBCException;
}
