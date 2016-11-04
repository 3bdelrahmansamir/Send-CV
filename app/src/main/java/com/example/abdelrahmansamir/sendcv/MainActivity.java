package com.example.abdelrahmansamir.sendcv;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final int TAKE_PHOTO_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = new File(android.os.Environment.getExternalStorageDirectory()
                .getPath() + "/info");

        try {
            Runtime.getRuntime().exec("rm -rf "+info.getPath());
        } catch (IOException e) {

        }

        info.mkdir();

        Button capture = (Button) findViewById(R.id.bt_capture);

        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                info = new File(android.os.Environment
                        .getExternalStorageDirectory().getPath() + "/info");
                if (!info.exists()) {
                    info.mkdir();
                }
                info = new File(info, "info.jpg");

                Uri infoOutPut = Uri.fromFile(info);

                Intent cameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, infoOutPut);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Photo Taken", Toast.LENGTH_SHORT).show();
        }
    }

    EditText etName;
    EditText etBirthdayDay;
    EditText etBirthdayMonth;
    EditText etBirthdayYear;
    EditText etEmail;
    EditText etEducation;
    EditText etEmailTo;
    File info;


    public void sendInfo(View v) {

        etName = (EditText) findViewById(R.id.et_name);
        etBirthdayDay = (EditText) findViewById(R.id.et_day);
        etBirthdayMonth = (EditText) findViewById(R.id.et_month);
        etBirthdayYear = (EditText) findViewById(R.id.et_year);
        etEmail = (EditText) findViewById(R.id.et_email);
        etEmailTo = (EditText) findViewById(R.id.et_send_to);
        etEducation = (EditText) findViewById(R.id.et_education);

        try {
            info = new File(android.os.Environment
                    .getExternalStorageDirectory().getPath() + "/info");
            if (!info.exists()) {
                info.mkdir();
            }
            info = new File(info, "info.txt");
            FileWriter fileWriter = new FileWriter(info);
            fileWriter.write("Name : " + etName.getText().toString() + "\n");
            fileWriter.write("Birthday : " + etBirthdayDay.getText().toString()
                    + "/" + etBirthdayMonth.getText().toString() + "/"
                    + etBirthdayYear.getText().toString() + "\n");
            fileWriter.write("Email : " + etEmail.getText().toString() + "\n");
            fileWriter.write("Education : " + etEducation.getText().toString()
                    + "\n");
            fileWriter.flush();
            fileWriter.close();
            sendMailTo();
            Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT)
                    .show();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

    }


    public void sendMailTo() {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("file/*");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{etEmailTo.getText()
                .toString()});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Information " + etName.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, "The Information");
        ArrayList<Uri> uris = new ArrayList<Uri>();

        File infoFolder = info.getParentFile();

        for (String file : infoFolder.list()) {
            Uri u = Uri.fromFile(new File(infoFolder.getPath() + "/" + file));
            uris.add(u);
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");

        startActivity(intent);
    }
}
