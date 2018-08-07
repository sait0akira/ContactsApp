package akira.contacts;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.Context;

public class CSVconvert extends AsyncTask<Void,Void,Void> {
    InputStream inputStream;
Realm realm;


    @Override
    protected Void doInBackground(Void... voids) {


        return null;
    }

    public CSVconvert(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void addToBase(android.content.Context context) {

        try {

            realm = Realm.getInstance(context);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                realm.beginTransaction();
                contact_shelf contact = realm.createObject(contact_shelf.class);
                contact.setName(row[0]);
                contact.setSurname(row[1]);
                contact.setPhoneNum(row[2]);
                contact.setBdate(row[3]);
                contact.setGender(row[4]);
                realm.commitTransaction();
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }

    }
}