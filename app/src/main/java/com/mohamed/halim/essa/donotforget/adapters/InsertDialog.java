package com.mohamed.halim.essa.donotforget.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.mohamed.halim.essa.donotforget.R;


public class InsertDialog extends AppCompatDialogFragment {
    // edit text to get the task name
    private EditText mTaskEditText;
    // time picker to get the time for the task
    private TimePicker mTimePicker;
    // an listener to insert the data
    private AddTaskListener mAddListener;

    /**
     * constructor to create a dialog
     * @param addListener : the add listener object
     */
    public InsertDialog(AddTaskListener addListener) {
        this.mAddListener = addListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // view inflated from @R.layout.insert_dialog
        View view = getActivity().getLayoutInflater().inflate(R.layout.insert_dialog,null);
        // get the edit text view @R.id.tast_et
        mTaskEditText = view.findViewById(R.id.task_et);
        // get the timer picker view @R.id.task_time
        mTimePicker = view.findViewById(R.id.task_time);
        // a dialog builder for the insert info
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // set the nigative button to dismiss the dialog
        dialogBuilder.setNegativeButton(R.string.cancel_dialog_button, dialogNegative());
        // set the positive button to insert the task
        dialogBuilder.setPositiveButton(R.string.add_dialog_button, dialogPositive());
        // set the view of the dialog to R.layout.insert_dialog
        dialogBuilder.setView(view);
        // set the title of the dialog
        dialogBuilder.setTitle(R.string.dialog_title);
        return dialogBuilder.create();
    }

    /**
     * send the data th the task listener
     */
    private DialogInterface.OnClickListener dialogPositive() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String task = mTaskEditText.getText().toString();
                long time = mTimePicker.getCurrentHour() * 60  + mTimePicker.getCurrentMinute();
                mAddListener.addTask(task, time);
            }
        };
    }

    /**
     * dissmiss the dialog
     */
    private DialogInterface.OnClickListener dialogNegative() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
    }

    /**
     *  an interface to listen to the add click
     */
    public interface AddTaskListener{
        void addTask(String task,long time);
    }


}
