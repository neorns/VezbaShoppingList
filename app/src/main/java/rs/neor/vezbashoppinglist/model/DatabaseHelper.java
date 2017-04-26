package rs.neor.vezbashoppinglist.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "shopinglists.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<ShoppingListItem, Integer> mShoppingListItemDao = null;
    private Dao<ShoppingList, Integer> mShoppingListDao = null;

    //obavezan konstruktor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ShoppingList.class);
            TableUtils.createTable(connectionSource, ShoppingListItem.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ShoppingListItem.class, true);
            TableUtils.dropTable(connectionSource, ShoppingList.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //Dao objekat za svaku tabelu
    public Dao<ShoppingListItem, Integer> getShoppingListItemDao() throws SQLException {
        if (mShoppingListItemDao == null) {
            mShoppingListItemDao = getDao(ShoppingListItem.class);
        }

        return mShoppingListItemDao;
    }
    public Dao<ShoppingList, Integer> getShoppingListDao() throws SQLException {
        if (mShoppingListDao == null) {
            mShoppingListDao = getDao(ShoppingList.class);
        }

        return mShoppingListDao;
    }

    @Override
    public void close() {
        mShoppingListItemDao = null;
        mShoppingListDao = null;

        super.close();
    }


}


