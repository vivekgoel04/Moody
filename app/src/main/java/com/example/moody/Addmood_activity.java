package com.example.moody;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.io.IOException;
import java.text.DateFormat;

import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

public class Addmood_activity extends ActionBarActivity {
	ImageView imageface;
	ImageView imagelaugh;
	ImageView imagecry;
	ImageView imagesmile;
	ImageView imagetired;
	ImageView imageheart;
	Button save, cancel;
	EditText edit1;
	TextView datetime, location;
	String i;
	String pos, dt, et;

	Singleton myDb = Singleton.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmood_activity);
		
		performOperation();
	}
	
	private void performOperation() {
		openDB();
		// To handle edit
		if (myDb.edit == true)
			editSetListViewContent();
		else
			i="0";
		// Called to calculate location
		setLocation();

		String currentDateTimeString = DateFormat.getDateTimeInstance().format(
				new Date());
		// textView is the TextView view that should display it
		datetime = (TextView) findViewById(R.id.datetime);
		datetime.setText(currentDateTimeString);
		imageface = (ImageView) findViewById(R.id.face);
		imagelaugh = (ImageView) findViewById(R.id.laugh);
		imagecry = (ImageView) findViewById(R.id.cry);
		imagesmile = (ImageView) findViewById(R.id.smile);
		imagetired = (ImageView) findViewById(R.id.tired);
		imageheart = (ImageView) findViewById(R.id.heart);
		imagelaugh.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				i = "1";
				imageface.setImageResource(R.drawable.laugh);
			}
		});
		imagecry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				i = "2";
				imageface.setImageResource(R.drawable.cry);
			}
		});
		imagesmile.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				i = "3";
				imageface.setImageResource(R.drawable.smile);
			}
		});
		imagetired.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				i = "4";
				imageface.setImageResource(R.drawable.tired);
			}
		});
		imageheart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				i = "5";
				imageface.setImageResource(R.drawable.heart);
			}
		});

		save = (Button) findViewById(R.id.button2);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Addmood_activity.this,
						MainActivity.class);
				openDB();
				location = (TextView) findViewById(R.id.tvLocation);
				pos = location.getText().toString();
				dt = datetime.getText().toString();
				imageface = (ImageView) findViewById(R.id.face);
				edit1 = (EditText) findViewById(R.id.etMessage);
				et = edit1.getText().toString();
				if ((i.equals("1")) || (i.equals("2")) || (i.equals("3"))
						|| (i.equals("4")) || (i.equals("5")))
					i = i;
				else
					i = "0";
				if (myDb.edit == true)
					myDb.updateRow(myDb.editRowId, i, pos, dt, et);
				else
					myDb.insertRow(i, pos, dt, et);

				myDb.edit = false;
				closeDB();
				startActivity(intent);

			}
		});

		cancel = (Button) findViewById(R.id.button1);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Addmood_activity.this,
						MainActivity.class);
				startActivity(intent);

			}
		});
	}
	
	protected void onNewIntent(Intent intent) {
		performOperation();
	}
	// Location Function
	private void setLocation() {
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				makeUseOfNewLocation(location);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	// Function use goecoder class to convert longitude and lattitude into
	// address
	private void makeUseOfNewLocation(Location location) {
		// TODO Auto-generated method stub
		TextView Location = (TextView) findViewById(R.id.tvLocation);
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);

			Address address = addresses.get(0);
			
			String stAddress = address.getAddressLine(0);
			String city = address.getLocality();
			String state = address.getAdminArea();
			String completeaddress = stAddress+", "+city+", "+state;
			Location.setText(completeaddress);
			
		} catch (IOException ioException) {
			Location.setText("Error");
		}

	}

	// Edit function
	private void editSetListViewContent() {
		openDB();

		// Extract face emoticon and display it
		String face = myDb.getFace(myDb.editRowId);
		imageface = (ImageView) findViewById(R.id.face);
		if (face.equals("1")) {
			imageface.setImageResource(R.drawable.laugh);
		} else if (face.equals("2")) {
			imageface.setImageResource(R.drawable.cry);
		} else if (face.equals("3")) {
			imageface.setImageResource(R.drawable.smile);
		} else if (face.equals("4")) {
			imageface.setImageResource(R.drawable.tired);
		} else if (face.equals("5")) {
			imageface.setImageResource(R.drawable.heart);
		} else
			imageface.setImageResource(R.drawable.face);

		i=face;
		
		// Extract Message and display it
		String message = myDb.getMessage(myDb.editRowId);
		edit1 = (EditText) findViewById(R.id.etMessage);
		edit1.setText(message);

		closeDB();
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
		getMenuInflater().inflate(R.menu.addmood_activity, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
