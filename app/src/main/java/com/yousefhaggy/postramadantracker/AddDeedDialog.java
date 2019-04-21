package com.yousefhaggy.postramadantracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDeedDialog extends DialogFragment {
    public interface AddDeedDialogListener{
        public void onAddDeedDialogPositiveClick(DialogFragment dialog);
    }
    AddDeedDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View mainView= inflater.inflate(R.layout.add_deed_dialog,null);
        final TextView deedTextInput= (TextView) mainView.findViewById(R.id.deedDescriptionTextView);
        Button submitButton = (Button) mainView.findViewById(R.id.addDeedButton);
        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrackerDatabaseHelper dbHelper= new TrackerDatabaseHelper(getContext());
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                Date currentTime=Calendar.getInstance().getTime();
                DateFormat df = new SimpleDateFormat("MM/dd/yy");
                String date=df.format(currentTime);
                db.execSQL("INSERT INTO MainTable values('"+deedTextInput.getText().toString()+"','"+date+"')");
                listener.onAddDeedDialogPositiveClick(AddDeedDialog.this);
                dismiss();
            }
        });
        builder.setView(mainView).setTitle("Add Deed");
        return builder.create();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try{
            listener =(AddDeedDialogListener) context;
        }
        catch (ClassCastException e) {

        }
        }
    }


