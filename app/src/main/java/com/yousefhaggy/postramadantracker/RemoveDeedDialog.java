package com.yousefhaggy.postramadantracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RemoveDeedDialog extends DialogFragment {
    public interface removeDeedDialogInterface{
        public void onRemoveDeedYes();
    }
    removeDeedDialogInterface listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       final String deed=getArguments().getString("deed");
        builder.setTitle("Have you recently performed this deed?").setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TrackerDatabaseHelper dbHelper= new TrackerDatabaseHelper(getContext());
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                Date currentTime= Calendar.getInstance().getTime();
                DateFormat df = new SimpleDateFormat("MM/dd/yy");
                String date=df.format(currentTime);
                db.execSQL("UPDATE MainTable SET date='"+date+"' WHERE description='"+deed+"'");
                listener.onRemoveDeedYes();
            }
        });
        return builder.create();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try{
            listener =(removeDeedDialogInterface) context;
        }
        catch (ClassCastException e) {

        }
    }
}
