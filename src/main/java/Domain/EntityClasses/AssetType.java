package domain.entityclasses;

// describes the type of an asset
public interface AssetType{

  //set get asset id
  public abstract long getAssetId();
  public abstract void setAssetId(long id);

  // return type id (loaded from file or some such)
  public abstract long getAssetType();
  // check if this asset has the same type as another
  public default boolean equals(AssetType at){
    return this.getAssetType() == at.getAssetType();
  }

}
