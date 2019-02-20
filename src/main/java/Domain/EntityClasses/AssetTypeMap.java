package domain.entityclasses;

import java.util.HashMap;

public class AssetTypeMap{
  public static long getAssetId(String name){
    if (assetMap == null){
      initAssetMap();
    }
    return assetMap.get(name);
  }

  private static void initAssetMap(){
    assetMap = new HashMap<String, Long>();
    assetMap.put("null", -1l);
    assetMap.put("Employee",0l);
  }

  private static HashMap<String, Long> assetMap = null;;
}
