package rs.neor.vezbashoppinglist.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rs.neor.vezbashoppinglist.R;
import rs.neor.vezbashoppinglist.adapters.ShoppingListAdapter;
import rs.neor.vezbashoppinglist.model.DatabaseHelper;
import rs.neor.vezbashoppinglist.model.ShoppingList;

public class MainActivity extends AppCompatActivity implements ShoppingListAdapter.UpdateListData {

    private ListAdapter adapter;
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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    addShoppingList();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        try {
            List<ShoppingList> list = getDatabaseHelper().getShoppingListDao().queryForAll();
            sortList(list);

            //adapter = new ArrayAdapter<ShoppingList>(this, R.layout.list_item_shoppinglist, list);
            adapter = new ShoppingListAdapter(this,R.layout.list_item_shoppinglist,list);

            final ListView listView = (ListView)this.findViewById(R.id.lv_shoppinglist);

            listView.setAdapter(adapter);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deleteList(ShoppingList shoppingList){
        try {
            getDatabaseHelper().getShoppingListDao().delete(shoppingList);
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void startDetailActivity(ShoppingList shoppingList){
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("shoppinglist_id", shoppingList.getmId());
        startActivity(intent);
    }
    @Override
    public void refresh() {
        ListView listview = (ListView) findViewById(R.id.lv_shoppinglist);

        if (listview != null){
            //ArrayAdapter<ShoppingList> adapter = (ArrayAdapter<ShoppingList>) listview.getAdapter();
            ShoppingListAdapter adapter = (ShoppingListAdapter) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<ShoppingList> list = getDatabaseHelper().getShoppingListDao().queryForAll();
                    sortList(list);

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sortList(List<ShoppingList> list){
        Collections.sort(list, new Comparator<ShoppingList>() {
            @Override
            public int compare(ShoppingList sl1, ShoppingList sl2) {
                int result=0;
                int s1=1;
                int s2=1;
                if (sl1.isCompleted()){s1=-1;}
                if (sl2.isCompleted()){s2=-1;}

                return s2*sl2.getmId()-s1*sl1.getmId();
            }
        });
    }

    private void addShoppingList() throws SQLException {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_shoppinglist);

        final EditText etName = (EditText) dialog.findViewById(R.id.et_name);

        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();

                ShoppingList shoppingList = new ShoppingList();
                shoppingList.setmName(name);

                try {
                    getDatabaseHelper().getShoppingListDao().create(shoppingList);
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

    // onCreateOptionsMenu method initialize the contents of the Activity's Toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
