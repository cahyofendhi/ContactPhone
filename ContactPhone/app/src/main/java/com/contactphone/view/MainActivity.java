package com.contactphone.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.contactphone.R;
import com.contactphone.adapter.ContactAdapter;
import com.contactphone.model.PhoneModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG  = "result";

    private final int PERMISSION_REQUEST_CONTACT  = 99;

    ContactAdapter contactAdapter;
    List<PhoneModel> phoneList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerViews();
        askForContactPermission();
    }

    /**
     * setup phone recycleview
     */
    private void initRecyclerViews() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        contactAdapter  = new ContactAdapter(this, phoneList);
        mRecyclerView.setAdapter(contactAdapter);
    }

    /**
     * check permission for android M and up
     */
    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                                                                         PERMISSION_REQUEST_CONTACT);
                }
            }else{
                getContact();
            }
        }else{
            getContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();
                } else {
                    Toast.makeText(this, "No permission for contacts", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * get contact from phone
     */
    private void getContact(){
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext()) {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.d(TAG, "name : "+name+" - number : "+phoneNumber);
            phoneList.add(new PhoneModel(name, phoneNumber));
        }
        phones.close();
        contactAdapter.notifyDataSetChanged();
    }

}
