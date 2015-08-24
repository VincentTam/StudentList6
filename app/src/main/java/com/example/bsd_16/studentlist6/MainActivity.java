package com.example.bsd_16.studentlist6;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {

    protected EditText searchText;
    protected SQLiteDatabase db;
    protected Cursor cursor;
    protected ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = (new DatabaseHelper(this)).getWritableDatabase();
        searchText = (EditText) findViewById(R.id.searchText);

        ImageButton button = (ImageButton) findViewById(R.id.searchButton);  // The back button to MainActivity.java
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Jump to function nav2Main()
                 * Why don't I just paste the function body of nav2Main() here?
                 */
                search(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void search(View view) {
        // || is the concatenation operation in SQLite
        cursor = db.rawQuery("SELECT _id, firstName, lastName, title FROM employee WHERE firstName || ' ' || lastName LIKE ?",
                new String[]{"%" + searchText.getText().toString() + "%"});
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.employee_list_item,
                cursor,
                new String[]{"firstName", "lastName", "title"},
                new int[]{R.id.firstName, R.id.lastName, R.id.title},
                0);
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView parent, View view, int position, long id) {
        Intent intent = new Intent(this, EmployeeDetails.class);
        Cursor cursor = (Cursor) adapter.getItem(position);
        intent.putExtra("EMPLOYEE_ID", cursor.getInt(cursor.getColumnIndex("_id")));
        startActivity(intent);
    }
}
