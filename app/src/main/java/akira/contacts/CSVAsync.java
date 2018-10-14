package akira.contacts;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class CSVAsync extends AsyncTask<Void, Void, Void> {
    private Context context;

    CSVAsync(Context context) {
        this.context = context;

    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            String UplURL = "https://www.sample-videos.com/csv/Sample-Spreadsheet-10-rows.csv"; //download URL
            URL url = new URL(UplURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            File apkStorage = new File(Environment.getExternalStorageDirectory() + "/csvContactsTest/"); //download folder
            if (!apkStorage.exists()) {
                apkStorage.mkdir();
            }
            String downlFileName = "Sample-Spreadsheet-10-rows.csv"; //downloaded filename

            File OutFile = new File(apkStorage, downlFileName);
            if (!OutFile.exists()) {
                OutFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(OutFile);
            InputStream is = connection.getInputStream();

            byte[] buffer = new byte[1024];
            int leng;
            while ((leng = is.read(buffer)) != -1) {
                fos.write(buffer, 0, leng);
            }

            fos.close();
            is.close();
            InputStream Fis = new FileInputStream(OutFile);
            new CSVconvert(Fis, context).execute();
           connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context,"Contacts have been downloaded.",Toast.LENGTH_SHORT).show();
    }
}
