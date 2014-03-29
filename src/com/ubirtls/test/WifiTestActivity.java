package com.ubirtls.test;

import com.ubirtls.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class WifiTestActivity extends Activity implements OnClickListener {
	private Button scanButton = null;

	// private Spotter s = null;
	// private Intent serviceIntent = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// set up UI
		/*
		 * scanButton = (Button)this.findViewById(R.id.scanButton);
		 * scanButton.setOnClickListener(this); s = new WiFiSpotter(this);
		 */
		// serviceIntent = new Intent(this,WiFiService.class);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	/*
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub //click the scan button if(v.getId() == R.id.scanButton) { //start
	 * service //this.startService(serviceIntent); Log.i("message", "click");
	 * String message = ""; try { s.open(); BeaconMeasurement m =
	 * (BeaconMeasurement) s.getMeasurement(); message += m.getReadingNumber() +
	 * "APs were seen\n"; if (m.getReadingNumber() > 0) { // Iterate through the
	 * Vector and print the readings for (int i = 0; i < m.getReadingNumber();
	 * i++) { WiFiReading r = (WiFiReading) m.getReadingByIndex(i); message +=
	 * r.getId() + " " + r.getSsid() + " " + r.getRssi() + "\n"; }
	 * Toast.makeText(this, message, Toast.LENGTH_LONG).show(); } s.close(); }
	 * catch (SpotterException ex) { ex.printStackTrace(); } } }
	 */
}