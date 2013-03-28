package com.jasonmcreynolds.dslvdbdemo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ItemAddEditActivity extends SherlockActivity {

	public static String EDIT_ITEM = "edit_item";
	public static String ITEM_ID = "item_id";
	private DatabaseAdapter mDbHelper;
	private Boolean mEdit = false;
	private Bundle mExtras;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_add_edit);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// check for extras to see if we are adding or editing
		mExtras = getIntent().getExtras();
		if (mExtras != null) {
			if (mExtras.getString(EDIT_ITEM).equals("edit"))
				mEdit = true;
		} else {
			mEdit = false;
		}

		// setup and open connection to database
		mDbHelper = DatabaseAdapter.getInstance(getBaseContext());
		mDbHelper.openConnection();

		if (mEdit == true) {
			populateItemDetails(mExtras.getLong(ITEM_ID));
		}
	}

	public void populateItemDetails(long id) {
		Cursor cursor = mDbHelper.getItemRecord(id);
		((TextView) findViewById(R.id.item_name)).setText(cursor
				.getString(cursor.getColumnIndex("item_name")));
		((TextView) findViewById(R.id.item_details)).setText(cursor
				.getString(cursor.getColumnIndex("item_details")));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.activity_item_add_edit, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, CursorDSLV.class));
			return (true);
		case R.id.cancel:
			finish();
			return (true);
		case R.id.save:
			if (mEdit == true) {
				mDbHelper.updateItemRecord(mExtras.getLong(ITEM_ID),
						((EditText) findViewById(R.id.item_name)).getText()
								.toString(),
						((EditText) findViewById(R.id.item_details)).getText()
								.toString());
			} else {
				mDbHelper.insertItemRecord(
						((EditText) findViewById(R.id.item_name)).getText()
								.toString(),
						((EditText) findViewById(R.id.item_details)).getText()
								.toString());
			}
			Toast.makeText(this, "Note saved.", Toast.LENGTH_SHORT).show();
			finish();
			return (true);

		}
		return (super.onOptionsItemSelected(item));
	}

}
