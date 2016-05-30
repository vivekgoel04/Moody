package com.example.moody;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
	private ImageButton btn = null;
	private TextView text = null;
	private ListView list;
	private PopupWindow pwindo;
	private ArrayAdapter<String> adapter;
	private List<CustomList> myList = new ArrayList<CustomList>();
	Singleton myDb = Singleton.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		performAllOperations();
	}

	private void performAllOperations() {
		list = (ListView) findViewById(R.id.listview);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				AlertDialog.Builder a_builder = new AlertDialog.Builder(
						MainActivity.this);
				a_builder.setMessage("What do you want to do?");
				a_builder.setCancelable(false);
				a_builder.setPositiveButton("Edit",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								openDB();
								long rowId = myDb.getRowId(position);
								Intent intent = new Intent(MainActivity.this,
										Addmood_activity.class);
								myDb.edit = true;
								myDb.editRowId = rowId;
								closeDB();
								startActivity(intent);
							}
						});
				a_builder.setNegativeButton("Delete",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								deleteRow(position);
							}
						});
				a_builder.setNeutralButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
				AlertDialog alert = a_builder.create();
				alert.setTitle("Alert !!!");
				alert.show();
				return false;
			}
		});
		openDB();
		Cursor cursor = myDb.getAllRows();
		displayRecordSet(cursor);
		closeDB();
	}

	protected void onNewIntent(Intent intent) {
		myList.clear();
		performAllOperations();
	}
	
	
	private void deleteRow(int position) {
		openDB();
		long RowId = myDb.getRowId(position);
		myDb.deleteRow(RowId);
		closeDB();
		Intent intent = new Intent(MainActivity.this, MainActivity.class);
		startActivity(intent);
	}
	private void displayRecordSet(Cursor cursor) {
		if (cursor.moveToFirst()) {
			do {
				text = (TextView) findViewById(R.id.viewText1);
				text.setVisibility(View.GONE);
				// Process the data
				int id = cursor.getInt(Singleton.COL_ROWID);
				String face = cursor.getString(myDb.COL_FACE);
				String pos = cursor.getString(myDb.COL_POSITION);
				String dt = cursor.getString(myDb.COL_DATETIME);
				String et = cursor.getString(myDb.COL_MESSAGE);
				dt="On "+pos+" at "+dt;
				populateCustomList(face, dt, et);
				populateListView();
			} while (cursor.moveToNext());
		}
		cursor.close();
		closeDB();
	}

	// Function called to populate CustomList
	public void populateCustomList(String face, String dt, String et) {
		if (face.equals("1")) {
			myList.add(new CustomList(R.drawable.laugh,dt, et));
		} else if (face.equals("2")) {
			myList.add(new CustomList(R.drawable.cry,dt, et));
		} else if (face.equals("3")) {
			myList.add(new CustomList(R.drawable.smile, dt, et));
		} else if (face.equals("4")) {
			myList.add(new CustomList(R.drawable.tired, dt, et));
		} else if (face.equals("5")) {
			myList.add(new CustomList(R.drawable.heart, dt, et));
		} else
			myList.add(new CustomList(R.drawable.face, dt, et));
	}

	private void populateListView() {
		ArrayAdapter<CustomList> adapter = new MyListAdapter();
		ListView list = (ListView) findViewById(R.id.listview);
		list.setAdapter(adapter);
	}

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
			// Find the list to work with
			CustomList currentList = myList.get(position);

			// Fill the view
			ImageView imageView = (ImageView) itemView.findViewById(R.id.face);
			imageView.setImageResource(currentList.getIconID());


			// Location & Date & Time:
			TextView dateTime = (TextView) itemView.findViewById(R.id.datetime);
			dateTime.setText("" + currentList.getLocationdatetime());

			// Message:
			TextView message = (TextView) itemView.findViewById(R.id.message);
			message.setText(currentList.getMsg());

			return itemView;
		}

	}

	public void addItem(View v) {
		Intent intent = new Intent(this, Addmood_activity.class);
		startActivity(intent);
	}

	private void openDB() {
		myDb.Helper(this);
		myDb = myDb.open();
	}

	private void closeDB() {
		myDb.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_close) {
			finish();
			System.exit(0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
