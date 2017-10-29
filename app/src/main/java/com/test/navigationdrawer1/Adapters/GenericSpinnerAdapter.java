package com.test.navigationdrawer1.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by osvaldo on 10/25/17.
 */

public class GenericSpinnerAdapter<T> extends BaseAdapter implements SpinnerAdapter {

    private Context context;
    /**
     * The internal data (the ArrayList with the Objects).
     */
    private final List<T> data;

    public GenericSpinnerAdapter(Context context, List<T> data){
        this.context = context;
        this.data = data;
    }

    /**
     * Returns the Size of the ArrayList
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * Returns one Element of the ArrayList
     * at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Returns the View that is shown when a element was
     * selected.
     */
    @Override
    public View getView(int position, View recycle, ViewGroup parent) {
        TextView text;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        text = (TextView) layoutInflater.inflate(
                android.R.layout.simple_dropdown_item_1line, parent, false
        );

        text.setTextColor(Color.BLACK);
        text.setText(data.get(position).toString());
        return text;
    }
}
