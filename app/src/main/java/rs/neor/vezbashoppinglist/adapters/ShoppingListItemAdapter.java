package rs.neor.vezbashoppinglist.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.neor.vezbashoppinglist.R;
import rs.neor.vezbashoppinglist.model.DatabaseHelper;
import rs.neor.vezbashoppinglist.model.ShoppingListItem;

public class ShoppingListItemAdapter extends ArrayAdapter<ShoppingListItem> {

    public interface UpdateItemData {
        void refresh();
        void deleteItem(ShoppingListItem shoppingListItem);
        void updateItem(ShoppingListItem shoppingListItem);
        void editItem(ShoppingListItem shoppingListItem);
    }

    UpdateItemData dataActivity;

    private List<ShoppingListItem> shoppingListItems = new ArrayList<>();

    private final LayoutInflater layoutInflater;

    private final Context context;

    public ShoppingListItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ShoppingListItem> shoppingListItems) {
        super(context, resource, shoppingListItems);
        this.shoppingListItems = shoppingListItems;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.dataActivity = (UpdateItemData) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.list_item_shoppinglistitem, parent, false);

            viewHolder.bought = (CheckBox) convertView.findViewById(R.id.cb_bought);
            viewHolder.name = (TextView) convertView.findViewById(R.id.text2);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.text3);
            viewHolder.btnDelete = (ImageButton) convertView.findViewById(R.id.btn_delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ShoppingListItem shoppingListItem = (ShoppingListItem) getItem(position);

        viewHolder.bought.setChecked(shoppingListItem.ismBought());
        viewHolder.name.setText(shoppingListItem.getmName());
        viewHolder.amount.setText(String.format("%1$.2f",shoppingListItem.getmAmount()));


        viewHolder.bought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingListItem.setmBought(viewHolder.bought.isChecked());
                shoppingListItems.set(position, shoppingListItem);
                dataActivity.updateItem(shoppingListItem);
            }
        });

        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataActivity.editItem(shoppingListItem);
            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataActivity.deleteItem(shoppingListItem);
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        private CheckBox bought;
        private TextView name;
        private TextView amount;
        private ImageButton btnDelete;
    }

    public List<ShoppingListItem> getShoppingLists() {
        return shoppingListItems;
    }

    public void setShoppingLists(List<ShoppingListItem> shoppingLists) {
        this.shoppingListItems = shoppingListItems;
    }


}


