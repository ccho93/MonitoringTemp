package com.monitoring.charles.monitoringtemp;

import android.app.DialogFragment;
import android.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public interface iSetDataFragment{
    public void onDialogPositiveClick(TempData temp);
    public void onDialogNegativeClick(TempData temp);

}
