package com.example.zac.myapplication.Dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.zac.myapplication.R;

/**
 * Created by zacattack101 on 2017-11-02.
 */

public class TagDialog extends DialogFragment {

    private String status = "";
    public static String search_text = "";

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        final Activity activity = getActivity();

        if (activity == null)
            return null;

        LayoutInflater inflater = activity.getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        status = "waiting";

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_tag_search, null);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        EditText et = (EditText) v.findViewById (R.id.tag_box);
                        if (et != null) {
                            search_text = et.getText().toString();
                            Log.e(":)", search_text);
                            status = "search";
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        status = "cancelled";
                    }
                });

        return builder.create();

    }

    public String getStatus () {
        return status;
    }

}
