package rs.neor.vezbashoppinglist.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import rs.neor.vezbashoppinglist.R;
import rs.neor.vezbashoppinglist.adapters.ShoppingListItemAdapter;
import rs.neor.vezbashoppinglist.model.DatabaseHelper;
import rs.neor.vezbashoppinglist.model.ShoppingList;
import rs.neor.vezbashoppinglist.model.ShoppingListItem;

public class DetailActivity extends AppCompatActivity  implements ShoppingListItemAdapter.UpdateItemData {
    private EditText name;
    private ListAdapter adapter;
    private ShoppingList currentShoppingList;
    private DatabaseHelper databaseHelper;
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addShoppingListItem(null);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });


        try {
            currentShoppingList = getDatabaseHelper().getShoppingListDao().queryForId(getIntent().getIntExtra("shoppinglist_id", -1));

            name =(EditText) this.findViewById(R.id.et_shoppinglistname);
            name.setText(currentShoppingList.getmName());



            //List<ShoppingListItem> list = getDatabaseHelper().getShoppingListItemDao().queryForAll();
            List<ShoppingListItem> list = getDatabaseHelper().getShoppingListItemDao().queryBuilder()
                    .where()
                    .eq(ShoppingListItem.SHOPPINGLISTITEM_FIELD_NAME_SHOPPINGLIST, currentShoppingList.getmId())
                    .query();

            //adapter = new ArrayAdapter<ShoppingListItem>(this, R.layout.list_item_shoppinglistitem, list);
            adapter = new ShoppingListItemAdapter(this,R.layout.list_item_shoppinglistitem,list);

            final ListView listView = (ListView)this.findViewById(R.id.lv_shoppinglistitem);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // trazimo id za bazu
                    ShoppingListItem shoppingListItem = (ShoppingListItem) listView.getItemAtPosition(position);

                    //Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    //intent.putExtra("shoppinglist_id", shoppingList.getmId());
                    //startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

        setTitle(currentShoppingList.getmName());
    }

    private void addShoppingListItem(final ShoppingListItem shoppingListItemOld) throws SQLException {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_shoppinglistitem);

        final EditText etName = (EditText) dialog.findViewById(R.id.et_name);
        final EditText etAmount = (EditText) dialog.findViewById(R.id.et_amount);
        if (shoppingListItemOld != null) {
            etName.setText(shoppingListItemOld.getmName());
            etAmount.setText(String.format("%1.2f",shoppingListItemOld.getmAmount()));
        }

        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                BigDecimal amount;
                try {
                    amount = new BigDecimal(etAmount.getText().toString());
                } catch (NumberFormatException e){
                    amount = new BigDecimal(0);
                }

                ShoppingListItem shoppingListItem = new ShoppingListItem();
                shoppingListItem.setShoppingList(currentShoppingList);
                shoppingListItem.setmName(name);
                shoppingListItem.setmAmount(amount);
                if (shoppingListItemOld != null) {
                    shoppingListItem.setmBought(shoppingListItemOld.ismBought());
                }
                else {
                    shoppingListItem.setmBought(false);
                }


                try {
                    getDatabaseHelper().getShoppingListItemDao().create(shoppingListItem);
                    refresh();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    @Override
    public void updateItem(ShoppingListItem shoppingListItem){
        try {
            getDatabaseHelper().getShoppingListItemDao().update(shoppingListItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void editItem(ShoppingListItem shoppingListItem){
        deleteItem(shoppingListItem);
        try {
            addShoppingListItem(shoppingListItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteItem(ShoppingListItem shoppingListItem){
        try {
            getDatabaseHelper().getShoppingListItemDao().delete(shoppingListItem);
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() {
        ListView listview = (ListView) findViewById(R.id.lv_shoppinglistitem);

        if (listview != null){
            //ArrayAdapter<ShoppingListItem> adapter = (ArrayAdapter<ShoppingListItem>) listview.getAdapter();
            ShoppingListItemAdapter adapter = (ShoppingListItemAdapter) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    //List<ShoppingListItem> list = getDatabaseHelper().getShoppingListItemDao().queryForAll();
                    List<ShoppingListItem> list = getDatabaseHelper().getShoppingListItemDao().queryBuilder()
                            .where()
                            .eq(ShoppingListItem.SHOPPINGLISTITEM_FIELD_NAME_SHOPPINGLIST, currentShoppingList.getmId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //sacuvaj izmene
        currentShoppingList.setmName(name.getText().toString());
        try {
            getDatabaseHelper().getShoppingListDao().update(currentShoppingList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
