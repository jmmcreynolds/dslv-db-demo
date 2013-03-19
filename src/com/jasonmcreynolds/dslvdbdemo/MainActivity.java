package com.jasonmcreynolds.dslvdbdemo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity implements OnClickListener {

	private DatabaseAdapter mDbHelper;
	private ImageButton mListButton;
	private Dialog mDummyAddDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// setup and open connection to database
		mDbHelper = new DatabaseAdapter(this);
		mDbHelper.openConnection();

		// wire button to layout
		mListButton = (ImageButton) findViewById(R.id.list_button);

		// setup the listener for the button
		mListButton.setOnClickListener(this);
	}

	public void resetDbConnection() {
		mDbHelper.closeConnection();
		mDbHelper = new DatabaseAdapter(this);
		mDbHelper.openConnection();
	}
	
	public void deleteAllLetterRecords() {
		resetDbConnection();
		mDbHelper.deleteAllItems();
		Toast.makeText(this, "DOH!!! YOU JUST DELETED EVERYTHING!",
		Toast.LENGTH_SHORT).show();
	}

	public void reloadAllLetterRecords() {
		resetDbConnection();
		deleteAllLetterRecords();
		mDbHelper.createInitialItemsDatabase();
		Toast.makeText(this, "WHEW!!! YOU JUST RESTORED THE DEFAULT DATA!",
				Toast.LENGTH_SHORT).show();
	}

	public void addDummyItems(int num_items) {
		mDbHelper.addDummyRecords(num_items);
	}

	public void displayDummyAddDialog() {
		mDummyAddDialog = new Dialog(MainActivity.this);
		mDummyAddDialog.setContentView(R.layout.dialog_dummy_item_add);
		mDummyAddDialog.findViewById(R.id.dslv_dummy_cancel).setOnClickListener(
				MainActivity.this);
		mDummyAddDialog.findViewById(R.id.dslv_dummy_ok).setOnClickListener(
				MainActivity.this);
		mDummyAddDialog.show();
	}

	@Override
	public void onClick(View buttonClick) {
		switch (buttonClick.getId()) {
		case R.id.list_button:
			Intent i = new Intent(this, CursorDSLV.class);
			startActivity(i);
			break;
		case R.id.dslv_dummy_cancel:
			mDummyAddDialog.dismiss();
			break;
		case R.id.dslv_dummy_ok:
			String number = ((EditText) mDummyAddDialog.findViewById(R.id.number_of_items_to_add)).getText().toString();
			int numItems = Integer.parseInt(number);
			addDummyItems(numItems);
			mDummyAddDialog.dismiss();
			
			Toast.makeText(this, numItems + " items added.", Toast.LENGTH_SHORT).show();
			break;
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.activity_main, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_dummy_notes:
			// runResetPrefs = true;
			displayDummyAddDialog();
			return (true);
		case R.id.delete_all_letter_records:
			deleteAllLetterRecords();
			return (true);
		case R.id.restore_all_letter_records:
			reloadAllLetterRecords();
			return (true);
		}
		return (super.onOptionsItemSelected(item));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mDbHelper != null) {
			mDbHelper.closeConnection();
		}
	}

}
