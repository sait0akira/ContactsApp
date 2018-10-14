package akira.contacts;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;



public class CSVconvert extends AsyncTask<Void, Void, Void> {
    InputStream inputStream;
    String csvLine;
    String[] td;
    Date tempDate;
    Context context;

    public CSVconvert(InputStream inputStream, Context context) {
        this.inputStream = inputStream;
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((csvLine = reader.readLine()) != null) {


                String[] row = csvLine.split(",");
                try {

                    String temp_string = row[3];
                    if (row[3].contains("/")) {
                        td = temp_string.split("/");
                        tempDate = new Date(Integer.parseInt(td[0]), Integer.parseInt(td[1]), Integer.parseInt(td[2]));
                    } else tempDate = null;

                    new AsyncManualContactUpload(context, null, row[0], row[1], row[2], tempDate, row[4]).execute();

                } catch (Exception ie) {
                    if (ie.getMessage().toLowerCase().contains("primary key")) {
                    }
                }
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

        return null;
    }


}