package rs.neor.vezbashoppinglist.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.neor.vezbashoppinglist.R;
import rs.neor.vezbashoppinglist.model.DatabaseHelper;
import rs.neor.vezbashoppinglist.model.ShoppingList;


public class ShoppingListAdapter  extends ArrayAdapter<ShoppingList> {

    public interface UpdateListData {
        void refresh();
        void startDetailActivity(ShoppingList shoppingList);
        void deleteList(ShoppingList shoppingList);
    }

    UpdateListData dataActivity;

    private List<ShoppingList> shoppingLists = new ArrayList<>();

    private final LayoutInflater layoutInflater;

    private final Context context;

    public ShoppingListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ShoppingList> shoppingLists) {
        super(context, resource, shoppingLists);
        this.shoppingLists = shoppingLists;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.dataActivity = (UpdateListData) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.list_item_shoppinglist, parent, false);

            viewHolder.text = (TextView) convertView.findViewById(R.id.text1);
            viewHolder.btnDelete = (ImageButton) convertView.findViewById(R.id.btn_delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ShoppingList shoppingList = (ShoppingList) getItem(position);

        viewHolder.text.setText(shoppingList.getmName());
        if (shoppingList.isCompleted()) {
            viewHolder.text.setBackgroundColor(0xff888888);
        }
        else {
            viewHolder.text.setBackgroundColor(0xffffffff);
        }

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataActivity.deleteList(shoppingList);
            }
        });

        viewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataActivity.startDetailActivity(shoppingList);
            }
        });

        return convertView;
    }

    private static class ViewHolder {

        private TextView text;
        private ImageButton btnDelete;
    }

    public List<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }
}

