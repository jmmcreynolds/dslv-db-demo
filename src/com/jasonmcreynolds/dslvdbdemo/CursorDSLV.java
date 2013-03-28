/*
 * This file is based on the CursorDSLV file that can be found
 * in the DragSortListView demo, available on github:
 *  
 *  https://github.com/bauerca/drag-sort-listview
 *  
 *  Special thanks to all those that have done so much to put
 *  DSLV together.
 */

package com.jasonmcreynolds.dslvdbdemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.SimpleDragSortCursorAdapter;

public class CursorDSLV extends SherlockFragmentActivity {

	private MAdapter mMAdapter;
	private DatabaseAdapter mDbHelper;
	private DragSortListView mDslv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cursor_main);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mDbHelper = DatabaseAdapter.getInstance(getBaseContext());
		mDbHelper.openConnection();

		displayItemList();
	}

	private void displayItemList() {
		// The desired columns to be bound
		String[] columns = new String[] { DatabaseAdapter.ITEM_NAME,
				DatabaseAdapter.ITEM_POSITION };

		// the XML defined views which the data will be bound to
		int[] ids = new int[] { R.id.item_name, R.id.item_position_list };

		// pull all items from database
		Cursor cursor = mDbHelper.getAllItemRecords();

		mMAdapter = new MAdapter(this, R.layout.list_items, null, columns, ids,
				0);

		mDslv = (DragSortListView) findViewById(R.id.item_list);

		// set dslv profile for faster scroll speeds
		mDslv.setDragScrollProfile(ssProfile);

		mDslv.setAdapter(mMAdapter);
		mMAdapter.changeCursor(cursor);

		mDslv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				// Get the cursor, positioned to the corresponding row in the
				// result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);

				// Get the item name and details from this row in the database.
				String itemName = cursor.getString(cursor
						.getColumnIndex("item_name"));
				String itemDetails = cursor.getString(cursor
						.getColumnIndex("item_details"));
				Toast.makeText(getApplicationContext(),
						itemName + ": " + itemDetails, Toast.LENGTH_SHORT)
						.show();
			}
		});

		mDslv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> listView, View view,
					int position, long id) {
				// Get the cursor, positioned to the corresponding row in the
				// result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);

				// Get the item name and details from this row in the database.
				long rowId = cursor.getLong(cursor.getColumnIndex("_id"));
				launchEditActivity(rowId);
				return true;
			}
		});
	}

	private void launchEditActivity(Long id) {
		Intent i = new Intent(this, ItemAddEditActivity.class);
		Log.d("putExtra", "ID: " + id);
		i.putExtra(ItemAddEditActivity.EDIT_ITEM, "edit");
		i.putExtra(ItemAddEditActivity.ITEM_ID, id);
		startActivity(i);
	}

	private DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
		@Override
		public float getSpeed(float w, long t) {
			if (w > 0.8f) {
				// Traverse all views in a millisecond
				return ((float) mMAdapter.getCount()) / 0.001f;
			} else {
				return 10.0f * w;
			}
		}
	};

	private class MAdapter extends SimpleDragSortCursorAdapter {

		public void persistChanges() {
			Cursor c = getCursor();
			c.moveToPosition(-1);
			while (c.moveToNext()) {
				int listPos = getListPosition(c.getPosition());
				if (listPos == REMOVED) {
					mDbHelper
							.deleteItemRecord(c.getInt(c.getColumnIndex("_id")));
				} else if (listPos != c.getPosition()) {
					mDbHelper.updateItemPosition(
							c.getInt(c.getColumnIndex("_id")), listPos);
				}
			}
		}

		public MAdapter(Context ctxt, int rmid, Cursor c, String[] cols,
				int[] ids, int something) {
			super(ctxt, rmid, c, cols, ids, something);
			mContext = ctxt;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			return v;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.activity_cursor, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
			return (true);
		case R.id.cancel:
			displayItemList();
			return (true);
		case R.id.save:
			mMAdapter.persistChanges();
			displayItemList();
			return (true);
		case R.id.add:
			Intent i = new Intent(this, ItemAddEditActivity.class);
			startActivity(i);
			break;
		}
		return (super.onOptionsItemSelected(item));
	}

	@Override
	protected void onResume() {
		super.onResume();
		Cursor cursor = mDbHelper.getAllItemRecords();
		mMAdapter.changeCursor(cursor);
	}

}
