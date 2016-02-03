package com.monitoring.charles.monitoringtemp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class SetDataFragment extends DialogFragment {

    private iSetDataFragment iDialog;

    public SetDataFragment() {
        // Required empty public constructor
    }
    public static SetDataFragment newInstance(){
        SetDataFragment f = new SetDataFragment();
        return f;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        final TempData tempData = new TempData();
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_set_data, null);
        final EditText text1 = (EditText) view.findViewById(R.id.test1);
        final EditText text2 = (EditText) view.findViewById(R.id.test2);
        final EditText text3 = (EditText) view.findViewById(R.id.test7);
        final EditText text4 = (EditText) view.findViewById(R.id.test8);
        final EditText text5 = (EditText) view.findViewById(R.id.test5);
        final EditText text6 = (EditText) view.findViewById(R.id.test6);
        final EditText text7 = (EditText) view.findViewById(R.id.test3);
        final EditText text8 = (EditText) view.findViewById(R.id.test4);
        final EditText text9 = (EditText) view.findViewById(R.id.test9);
        final EditText text10 = (EditText) view.findViewById(R.id.test10);
        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tempData.setPreheatTemp(Double.parseDouble(text1.getText().toString()));
                        tempData.setPreheatTime(Integer.parseInt(text2.getText().toString()));
                        tempData.setTemp1temp(Double.parseDouble(text3.getText().toString()));
                        tempData.setTemp1time(Integer.parseInt(text4.getText().toString()));
                        tempData.setTemp2temp(Double.parseDouble(text5.getText().toString()));
                        tempData.setTemp2time(Integer.parseInt(text6.getText().toString()));
                        tempData.setTemp3temp(Double.parseDouble(text7.getText().toString()));
                        tempData.setTemp3time(Integer.parseInt(text8.getText().toString()));
                        tempData.setCoolTemp(Double.parseDouble(text9.getText().toString()));
                        tempData.setCoolTempTime(Integer.parseInt(text10.getText().toString()));
                        Toast.makeText(getActivity(),"Saved Data",Toast.LENGTH_SHORT).show();
                        iDialog.onDialogPositiveClick(tempData);
                        //SetDataFragment.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetDataFragment.this.getDialog().cancel();

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iDialog = (iSetDataFragment) activity;
    }
}
