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
import io.realm.Realm;
import io.realm.RealmResults;

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


        class ContactLoader extends AsyncTask<Void, Void, Void> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(context, "Contacts Loaded", Toast.LENGTH_SHORT).show();

            }

            protected Cursor getContactsBirthday() {
                Uri uri = Data.CONTENT_URI;

                String[] projection = new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Event.START_DATE
                };

                String where =
                        Data.MIMETYPE + "= ? AND " +
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
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Note.NOTE
                };

                String where = null;

                String[] selectionArgs = null;
                String sortOrder = null;
                return managedQuery(uri, projection, where, selectionArgs, sortOrder);
            }


            @Override
            protected Void doInBackground(Void... voids) {
                cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);


                Cursor cursor = getContactsBirthday();
                Cursor cursor2 = getGender();


                while (cursor1.moveToNext()) {

                    realm = Realm.getInstance(context);
                    realm.beginTransaction();
                    contact_shelf contact;
                    contact = realm.createObject(contact_shelf.class);

                    RealmResults<contact_shelf> toDel;
                    


                    if (cursor2.moveToFirst()) {
                        do {
                            if ((cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).equals(cursor2.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))))
                                    && (cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)).toLowerCase().contains("male")))
                                contact.setGender(cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)));

                        } while (cursor2.moveToNext());

                    }


                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).equals(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))))
                                contact.setBdate(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)));
                        } while (cursor.moveToNext());
                    }// cursor.close();
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
                    toDel = realm.where(contact_shelf.class).equalTo("phoneNum", cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME))).findAll();
                    int sizee = toDel.size();
                    for (int i = sizee - 1; i > -1; i--)
                        toDel.get(i).removeFromRealm();
                    contact.setPhoneNum(cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)));


                    realm.commitTransaction();
                }
                // cursor1.

                return null;
            }
        }
        new ContactLoader().execute();


        if (savedInstanceState == null)

        {
            getSupportFragmentManager()
                    .beginTransaction()

                    .add(R.id.viewFrame, new contact_list())
                    .commit();

        }
    }

    class CSVAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                // String UplURL = "https://docs.google.com/spreadsheets/d/1hKb4Zvxh6he6M2ZCc0cHg7bqohrcXgf7Ggb-q9s8_2c/edit?usp=sharing";
                String UplURL = "http://samplecsvs.s3.amazonaws.com/SalesJan2009.csv";
                URL url = new URL(UplURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                File apkStorage = new File(Environment.getExternalStorageDirectory() + "/csvContactsTest/");
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                }
                String downlFileName = "SalesJan2009.csv";

                File OutFile = new File(apkStorage, downlFileName);
                // if (!OutFile.exists()) {
                OutFile.createNewFile();
                // }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    public boolean OnClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.new_contact) {
            Intent intent = new Intent(MainActivity.this, add_contact.class);
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