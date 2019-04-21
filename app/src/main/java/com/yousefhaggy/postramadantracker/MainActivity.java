package com.yousefhaggy.postramadantracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddDeedDialog.AddDeedDialogListener, RemoveDeedDialog.removeDeedDialogInterface{
    private static String ramadanEndDateString="06/04/19";
    Date ramadanEndDate;
    private boolean isRamadan=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView header= (TextView) findViewById(R.id.ramadanCountDown);
        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.floatingActionButton);
        ListView listView = (ListView)findViewById(R.id.deedListView);
        DateFormat df= new SimpleDateFormat("MM/dd/yy");;
        try {
            ramadanEndDate= df.parse(ramadanEndDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentTime= Calendar.getInstance().getTime();
        long difference=ramadanEndDate.getTime()-currentTime.getTime();
        long differenceInDays= (difference /(24*60*60*1000))+1;
        if (differenceInDays<=0)
        {
            isRamadan=false;
            header.setText("Keep up with your deed reminder list!");
            fab.setEnabled(false);
            fab.hide();

        }
        else {
            String daysTillRamdanEnds = Long.toString(differenceInDays);
            header.setText(daysTillRamdanEnds+" days left!");
        }
         ArrayAdapter<String> listAdapter= null;
        try {
            listAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item,getDeedsList());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        listView.setAdapter(listAdapter);
        if(!isRamadan)
        {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle bundle = new Bundle();
                    bundle.putString("deed",adapterView.getAdapter().getItem(i).toString());
                    RemoveDeedDialog dialog = new RemoveDeedDialog();
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(),null);
                }
            });
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDeedDialog dialog= new AddDeedDialog();
                dialog.show(getSupportFragmentManager(),null);
            }
        });
    }


    public  List<String> getDeedsList() throws ParseException {
        DateFormat df= new SimpleDateFormat("MM/dd/yy");;
        List<String> deedsList=new ArrayList<>();
        TrackerDatabaseHelper dbHelper= new TrackerDatabaseHelper(this);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM MainTable",null);
        if(isRamadan) {


            while (cursor.moveToNext()) {
                String deedDate = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String deed = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                deedsList.add(deed);
            }
        }
        else
        {
            Date currentTime= Calendar.getInstance().getTime();
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String deed = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                Date deedDate =df.parse(date);
                long difference=currentTime.getTime()-deedDate.getTime();
                long differenceInDays= (difference /(24*60*60*1000))+1;
                Log.e(deed,differenceInDays+"");

                if (differenceInDays>5){
                    deedsList.add(deed);
                }
            }
        }
        return  deedsList;
    }

    @Override
    public void onAddDeedDialogPositiveClick(DialogFragment dialog) {
        ListView listView = (ListView)findViewById(R.id.deedListView);
        ArrayAdapter<String> listAdapter= null;
        try {
            listAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item,getDeedsList());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        listView.setAdapter(listAdapter);

    }

    @Override
    public void onRemoveDeedYes() {
        ListView listView = (ListView)findViewById(R.id.deedListView);
        ArrayAdapter<String> listAdapter= null;
        try {
            listAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item,getDeedsList());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        listView.setAdapter(listAdapter);
    }
}
