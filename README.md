DragSortListView Database Demo
==============================

Overview
--------

This is a demo based off of the [DragSortListView library] (https://github.com/bauerca/drag-sort-listview) created by Carl A. Bauer.
The demo includes a working example of how to setup a DragSortListView that will
save the changes that you make to the list (position, add/delete) to a database.

The [ActionBarSherlock library] (http://actionbarsherlock.com/) is also required in order for this demo to run.

Functionality
-------------

Once the app is launched, tap on the icon to view the list.
Drag items around to re-arrange them and swipe to delete them.
Once you are done with the changes, click "Save" to save the changes.
If you don't want to save the changes, click "Cancel".
From the list view you can:
* Tap an item to get a toast that shows its details
* Long press an item to edit it
* Swipe an item left or right to delete it
* Press the menu key and then press "Add" to add an item.

From the main launch screen, you have several options once you press the menu key:
* **Delete All items** - clears everything from the database, no questions asked
* **Reset to default items** - clears everything from the database, no questions asked,
and then loads the default items that were available when the app was first launched
* **Add dummy notes** - Allows you to choose the number of dummy notes to add to the
database to test functionality. Notes will be named "Note X", where X is a number.
