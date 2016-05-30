/*package com.example.moody;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<CustomList> {
	public MyListAdapter() {
		super(MainActivity.this, R.layout.list_view, myList);
	}

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View itemView = convertView;
		if (itemView == null) {
			itemView = getLayoutInflater().inflate(R.layout.list_view,
					parent, false);
		}
		// Find the car to work with
		CustomList currentList = myList.get(position);

		// Fill the view
		ImageView imageView = (ImageView) itemView.findViewById(R.id.face);
		imageView.setImageResource(currentList.getIconID());

		// Location:
		TextView location = (TextView) itemView.findViewById(R.id.location);
		location.setText(currentList.getLocation());

		// Date & Time:
		TextView dateTime = (TextView) itemView.findViewById(R.id.datetime);
		dateTime.setText("" + currentList.getDatetime());

		// Message:
		TextView message = (TextView) itemView.findViewById(R.id.message);
		message.setText(currentList.getMsg());

		return itemView;
	}
	
	
}*/