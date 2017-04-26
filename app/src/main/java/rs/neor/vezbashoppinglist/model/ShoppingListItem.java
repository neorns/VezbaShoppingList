package rs.neor.vezbashoppinglist.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;


@DatabaseTable(tableName = ShoppingListItem.TABLE_NAME_SHOPPINGLISTITEM)
public class ShoppingListItem {

    public static final String TABLE_NAME_SHOPPINGLISTITEM = "shoppinglistitem";

    public static final String SHOPPINGLISTITEM_FIELD_NAME_ID = "id";
    public static final String SHOPPINGLISTITEM_FIELD_NAME_NAME = "name";
    public static final String SHOPPINGLISTITEM_FIELD_NAME_AMOUNT = "amount";
    public static final String SHOPPINGLISTITEM_FIELD_NAME_SHOPPINGLIST = "shoppinglist";
    public static final String SHOPPINGLISTITEM_FIELD_NAME_BOUGHT = "bought";

    @DatabaseField(columnName = SHOPPINGLISTITEM_FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = SHOPPINGLISTITEM_FIELD_NAME_NAME)
    private String mName;

    @DatabaseField(columnName = SHOPPINGLISTITEM_FIELD_NAME_AMOUNT)
    private BigDecimal mAmount;

    @DatabaseField(columnName = SHOPPINGLISTITEM_FIELD_NAME_BOUGHT)
    private boolean mBought;


    @DatabaseField(columnName = SHOPPINGLISTITEM_FIELD_NAME_SHOPPINGLIST, foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private ShoppingList shoppinglist;

    public ShoppingListItem() {
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

    public BigDecimal getmAmount() {
        return mAmount;
    }

    public void setmAmount(BigDecimal mAmount) {
        this.mAmount = mAmount;
    }

    public boolean ismBought() {
        return mBought;
    }

    public void setmBought(boolean mBought) {
        this.mBought = mBought;
    }

    public ShoppingList getShoppingList() {
        return shoppinglist;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppinglist = shoppingList;
    }
}
