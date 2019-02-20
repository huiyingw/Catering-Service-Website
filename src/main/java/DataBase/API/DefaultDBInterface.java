package database.api;

import java.util.function.Consumer;
import java.util.HashMap;
import java.util.ArrayList;

import database.query.StorableInterface;
import database.query.StorageDriverBase;
import database.query.StorageDriverException;
import database.query.StorageDriverMap;
import database.query.driver.DriverMapper;
import database.jdbc.JDBCConnectionManager;
import database.jdbc.JDBCConnectionType;
import database.jdbc.JDBCException;
import database.jdbc.JDBCExceptionType;


public class DefaultDBInterface implements DBInterface{

    protected DefaultDBInterface() throws JDBCException{
      DriverMapper.populateMap();
    }

    // singleton instance
    private static DefaultDBInterface ptrDefaultDBInterface = null;
    //get singleton
    public static DefaultDBInterface getInstance() throws JDBCException{
      if(ptrDefaultDBInterface == null){
        initialize();
      }

      return ptrDefaultDBInterface;
    }

    protected static void initialize() throws JDBCException{
      ptrDefaultDBInterface = new DefaultDBInterface();
      ptrDefaultDBInterface.conManager = ptrDefaultDBInterface.connectToDB();
    }

    protected JDBCConnectionManager connectToDB() throws JDBCException{
      try{
        return JDBCConnectionManager.getInstance(JDBCConnectionType.MYSQL);
      }
      catch(JDBCException e){
        //failed to connect to mysql try sqlite
        System.out.println("Failing over to Backup DB, SQLITE");
        JDBCConnectionManager con = JDBCConnectionManager.getInstance(JDBCConnectionType.SQLITE);
        //jank code incoming! no time, should refactor but, no time.
        try{
          DebugDBInterface dbI = DebugDBInterface.getInstance();
          dbI.initializeDB();
        }
        catch(DBException e2){
          //were screwed!
          return null;
        }
        return con;
      }
    }

    // store the passed object to the data base (see StorableInterface for more info)
    @Override
    public void store(StorableInterface objToStore) throws JDBCException, StorageDriverException{
      StorageDriverBase storageD = getStorageDriver(objToStore);

      //check if already in db.
      if(!conManager.query(storageD.toSelectSQL(objToStore)).isEmpty()){
        conManager.query(storageD.toReplaceSQL(objToStore));
      }
      else{
        conManager.query(storageD.toInsertSQL(objToStore));
      }
    }

    // load the passed object from the data base. (see StorableInterface for more info)
    @Override
    public void load(StorableInterface objToLoad) throws JDBCException, StorageDriverException{
      StorageDriverBase storageD = getStorageDriver(objToLoad);
      ArrayList<HashMap<String, Object>> res = conManager.query(storageD.toSelectSQL(objToLoad));
      storageD.fromSQLEmptyCheck(res,objToLoad);
    }

    // delete records of this object from the database.
    @Override
    public void delete(StorableInterface objToDelete) throws JDBCException, StorageDriverException{
      StorageDriverBase storageD = getStorageDriver(objToDelete);
      conManager.query(storageD.toDeleteSQL(objToDelete));
    }

    @Override
    public long getNextIdForTable(String tableName) throws JDBCException {

      try{
        ArrayList<HashMap<String,Object>> results = null;
        results = conManager.query("SELECT id FROM " + tableName + " ORDER BY  id DESC LIMIT 1;");

        if(results != null && results.size() > 0){
          return ((Number)results.get(0).get("id")).longValue() + 1;
        }
        else {
          // no results. perhaps table is empty? just return 0
          return 0;
        }
      }
      catch(JDBCException e){
        throw new JDBCException(e.getMessage() +
            "\nMost likly caused by lack of 'id' column in table " + tableName, e.getType());
      }
    }

    @Override
    public void registerStorageDriver(String name, StorageDriverBase driver){
      StorageDriverMap sMap = StorageDriverMap.getInstance();
      sMap.appendDriverMapping(name,driver);
    }

    @Override
    public void ensureConnection(JDBCConnectionType conType)throws JDBCException{
      if(conManager == null || !conManager.isConnected()){
        conManager = JDBCConnectionManager.getInstance(conType);
      }
    }

    // accepts a lambda function that will be passed the JDBCConnectionManager object
    // this lambda can do what ever it wants but, it is expected that it perform DB specific
    // initialization functions such as creating DATABASEs / TABLEs .... etc.
    @Override
    public void customInitializeDB(Consumer<JDBCConnectionManager> initFunction) throws JDBCException{
      initFunction.accept(conManager);
    }

    private StorageDriverBase getStorageDriver(StorableInterface obj) throws StorageDriverException{
      StorageDriverBase storageD = StorageDriverMap.getInstance().getStorageDriver(obj.getStorageDriver());
      if(storageD == null){
        throw new StorageDriverException("Could not find storage driver: " + obj.getStorageDriver());
      }
      return storageD;
    }

    protected JDBCConnectionManager conManager;
}
