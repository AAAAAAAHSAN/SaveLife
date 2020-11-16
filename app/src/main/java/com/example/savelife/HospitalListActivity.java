package com.example.savelife;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HospitalListActivity extends AppCompatActivity {

    ListView list;

    String[] maintitle ={
            "Chittagong Medical College Hospital",
            "CCC General Hospital","Chittagong General Hospital",
            "Max Hospital & Diagnostic Ltd.","Surgiscope Hospital Limited Unit-II",
            "Medical Centre Hospital","Lions General Hospital","Imperial Hospital Limited"
    };

    String[] subtitle ={
            "Ambulance Available","Ambulance Unavailable",
            "Ambulance Available","Ambulance Unavailable",
            "Ambulance Available","Ambulance Available",
            "Ambulance Available","Ambulance Unavailable"
    };

    Integer[] imgid={
            R.drawable.im1,R.drawable.im2,
            R.drawable.im1,R.drawable.im1,
            R.drawable.im2,R.drawable.im1,
            R.drawable.im2,R.drawable.im1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        MyListAdapter adapter=new MyListAdapter(this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if(position == 0) {
                    //code specific to first list item
                    new AlertDialog.Builder(HospitalListActivity.this)
                            .setTitle(maintitle[0])
                            .setMessage("Ambulance: 01546xxxxxxx")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Cancel", null).show();
                }

                else if(position == 1) {
                    new AlertDialog.Builder(HospitalListActivity.this)
                            .setTitle(maintitle[position])
                            .setMessage("Ambulance: 01546xxxxxxx")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Cancel", null).show();
                }

                else if(position == 2) {

                    new AlertDialog.Builder(HospitalListActivity.this)
                            .setTitle(maintitle[position])
                            .setMessage("Ambulance: 01546xxxxxxx")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Cancel", null).show();
                }
                else if(position == 3) {

                    new AlertDialog.Builder(HospitalListActivity.this)
                            .setTitle(maintitle[position])
                            .setMessage("Ambulance: 01546xxxxxxx")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Cancel", null).show();
                }
                else if(position == 4) {

                    new AlertDialog.Builder(HospitalListActivity.this)
                            .setTitle(maintitle[position])
                            .setMessage("Ambulance: 01546xxxxxxx")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Cancel", null).show();
                }
                else if(position == 5) {

                    new AlertDialog.Builder(HospitalListActivity.this)
                            .setTitle(maintitle[position])
                            .setMessage("Ambulance: 01546xxxxxxx")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Cancel", null).show();
                }
                else if(position == 6) {

                    new AlertDialog.Builder(HospitalListActivity.this)
                            .setTitle(maintitle[position])
                            .setMessage("Ambulance: 01546xxxxxxx")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Cancel", null).show();
                }
                else if(position == 7) {

                    new AlertDialog.Builder(HospitalListActivity.this)
                            .setTitle(maintitle[position])
                            .setMessage("Ambulance: 01546xxxxxxx")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Cancel", null).show();
                }
                else if(position == 8) {

                    new AlertDialog.Builder(HospitalListActivity.this)
                            .setTitle(maintitle[position])
                            .setMessage("Ambulance: 01546xxxxxxx")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Cancel", null).show();
                }

            }
        });
    }
}