package rs.neor.vezbashoppinglist.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName = ShoppingList.TABLE_NAME_SHOPPINGLIST)
public class ShoppingList {
    public static final String TABLE_NAME_SHOPPINGLIST = "shoppinglist";

    public static final String SHOPPINGLIST_FIELD_NAME_ID = "id";
    public static final String SHOPPINGLIST_FIELD_NAME_NAME = "name";
    public static final String SHOPPINGLIST_FIELD_NAME_COMPLETED = "completed";

    @DatabaseField(columnName = SHOPPINGLIST_FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = SHOPPINGLIST_FIELD_NAME_NAME)
    private String mName;

    @ForeignCollectionField(foreignFieldName = "shoppinglist", eager = true)
    private ForeignCollection<ShoppingListItem> items;



    public ShoppingList() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public ForeignCollection<ShoppingListItem> getItems() {
        return items;
    }

    public void setItems(ForeignCollection<ShoppingListItem> items) {
        this.items = items;
    }

    public boolean isCompleted() {
        boolean completed=false;
        if ((items != null) && !(items.isEmpty())){
            completed = true;
            for (ShoppingListItem item:items
                 ) {
                completed = completed && item.ismBought();
            }
        }


        return completed;
    }

    @Override
    public String toString() {
        return this.getmName();
    }
}
