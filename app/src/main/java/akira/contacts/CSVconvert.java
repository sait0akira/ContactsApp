package akira.contacts;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import io.realm.Realm;


public class CSVconvert extends AsyncTask<Void, Void, Void> {
    InputStream inputStream;
    Realm realm;
    String csvLine;
    String[] td;
    Date temp_data;


    @Override
    protected Void doInBackground(Void... voids) {

        return null;
    }

    public CSVconvert(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void addToBase(android.content.Context context) {

        try {

            realm = Realm.getDefaultInstance();


            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((csvLine = reader.readLine()) != null) {

                realm.executeTransaction(new Realm.Transaction() {

                    @Override
                    public void execute(Realm realm) {
                        String[] row = csvLine.split(",");
                        try {
                            Realm_contact contact = realm.createObject(Realm_contact.class, new MyFunctions().phoneNumCorrector(row[2]));
                            contact.setName(row[0]);
                            contact.setSurname(row[1]);
                            String temp_string = row[3];
                            td = temp_string.split("/");
                            temp_data = new Date(Integer.parseInt(td[0]), Integer.parseInt(td[1]), Integer.parseInt(td[2]));
                            contact.setBdate(temp_data);
                            contact.setGender(row[4]);
                        } catch (Exception ie) {
                            if (ie.getMessage().toLowerCase().contains("primary key")) {
                            }
                        }
                    }
                });
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } catch (Exception ie) {
            if (ie.getMessage().toLowerCase().contains("primary key")) {
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }

    }
}