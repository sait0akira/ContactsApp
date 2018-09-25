package akira.contacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public Cursor cursor1;
    public Context context;
    public Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        context = this.getApplicationContext();

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Realm.init(getApplicationContext());


        class ContactLoader extends AsyncTask<Void, Void, Void> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(context, "Contacts Loaded", Toast.LENGTH_SHORT).show();

            }


            protected Cursor getContactsBirthday() {
                Uri uri = ContactsContract.Data.CONTENT_URI;

                String[] projection = new String[]{
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Event.START_DATE
                };

                String where =
                        ContactsContract.Data.MIMETYPE + "= ? AND " +
                                ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;
                String[] selectionArgs = new String[]{
                        ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
                };
                String sortOrder = null;

                return managedQuery(uri, projection, where, selectionArgs, sortOrder);
            }


            protected Cursor getGender() {
                Uri uri = Data.CONTENT_URI;

                String[] projection = new String[]{
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Note.NOTE

                };

                String where = null;

                String[] selectionArgs = null;
                String sortOrder = null;
                return managedQuery(uri, projection, where, selectionArgs, sortOrder);
            }

            protected Cursor getAll() {
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                return managedQuery(uri, null, null, null, null);
            }


            @Override
            protected Void doInBackground(Void... voids) {

                final Cursor cursor1 = getAll();
                final Cursor getGender = getGender();
                final Cursor getBDATE = getContactsBirthday();
                getBDATE.moveToFirst();
                cursor1.moveToFirst();
                do {
                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {

                        Realm_contact contact;

                        @Override
                        public void execute(Realm realm) {

                            String contactNumber = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                            contactNumber = phoneNumCorrector(contactNumber);
                            contactNumber = new MyFunctions().phoneNumCorrector(contactNumber);
                            try {

                                contact = realm.createObject(Realm_contact.class, contactNumber);


                                contact.setName(cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));


                                //  Вбивание имени и фамилии в базу
                                String[] ns;
                                String altName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE));
                                ns = altName.split(",");
                                if (altName.contains("+")) {
                                } else {
                                    if (altName.contains(",")) {
                                        contact.setName(ns[1].trim());
                                        contact.setSurname(ns[0].trim());
                                    } else contact.setName(ns[0].trim());

                                }

//                                Вбивание даты рождения


                                getBDATE.moveToFirst();
                                do {
                                    if (getBDATE.getString(0).equals(cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))) {

                                        String[] temp_date = getBDATE.getString(2).split("-");
                                        contact.setBdate(new Date(Integer.parseInt(temp_date[0]), Integer.parseInt(temp_date[1]), Integer.parseInt(temp_date[2])));

                                    }

                                } while (getBDATE.moveToNext());

                                // Вбивание пола в базу

                                getGender.moveToFirst();
                                do {
                                    if (getGender.getString(0).equals(cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))) {
                                        if ((getGender.getString(2) != null) && (getGender.getString(2).toLowerCase().contains("male")))
                                            contact.setGender(getGender.getString(2));


                                    }
                                } while (getGender.moveToNext());


                            } catch (Exception ie) {
                                if (ie.getMessage().toLowerCase().contains("primary key")) {
                                }
                            }
                        }
                    });


                } while (cursor1.moveToNext());

                return null;
            }
        }
        new ContactLoader().execute();


        if (savedInstanceState == null)

        {
            getSupportFragmentManager()
                    .beginTransaction()

                    .add(R.id.viewFrame, new Recycler_contacts_list())
                    .commit();

        }
    }

    class CSVAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String UplURL = "http://samplecsvs.s3.amazonaws.com/SalesJan2009.csv"; //download URL
                URL url = new URL(UplURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                File apkStorage = new File(Environment.getExternalStorageDirectory() + "/csvContactsTest/"); //download folder
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                }
                String downlFileName = "SalesJan2009.csv"; //downloaded file`s name

                File OutFile = new File(apkStorage, downlFileName);
                if (!OutFile.exists()) {
                    OutFile.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(OutFile);
                InputStream is = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int leng = 0;
                while ((leng = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, leng);
                }
                fos.close();
                is.close();
                InputStream Fis = new FileInputStream(OutFile);
                CSVconvert csvCon = new CSVconvert(Fis);

                csvCon.addToBase(getBaseContext());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    public String phoneNumCorrector(String phn) {
        char[] temp_number = null;
        temp_number = phn.toCharArray();
        if ((temp_number[0] == '+') && (temp_number[1] == '7')) {
            temp_number[0] = '-';
            temp_number[1] = '8';
        }
        for (int i = 0; i < temp_number.length - 1; i++) {
            if ((temp_number[i] == ' ') || (temp_number[i] == '(') || (temp_number[i] == ')'))
                temp_number[i] = '-';


        }
        String finalNum = "";
        for (int i = 0; i < temp_number.length; i++) {
            if (temp_number[i] != '-') finalNum += String.valueOf(temp_number[i]);
        }
        return finalNum;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    public boolean OnClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.new_contact) {
            Intent intent = new Intent(MainActivity.this, New_contact.class);
            startActivity(intent);

        }

        if (menuItem.getItemId() == R.id.UploadCSV) {
            new CSVAsync().execute();


        }

        return true;
    }


    public void onBackPressed() {
        //  super.onBackPressed();

        openQuitDialog();
    }

    private void openQuitDialog() {
        final AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Выйти из приложения?");
        quitDialog.setPositiveButton("Выход", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        quitDialog.show();
    }


}