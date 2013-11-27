package com.aryan.donttextme.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.aryan.donttextme.R;

/**
 * Created by User on 11/27/13.
 */
public class AddToBlackList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_blacklist);

        EditText OriginatingAddress = (EditText) findViewById(R.id.originatingAddress);
        OriginatingAddress.setFocusable(false);
    }
}
