package akira.contacts;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.Date;

import io.realm.Realm;

class AsyncManualContactUpload extends AsyncTask<Void, Void, Void> {

    private boolean isCreated = true;
    private Context context;
    private Activity activity;
    private String trimmedName;
    private String trimmedSurname;
    private String phoneNum;
    Date date;
    private String gender;


    AsyncManualContactUpload(Context context, Activity activity, String trimmedName, String trimmedSurname, String phoneNum, Date bDate, String gender) {
        super();
        this.context = context;
        this.activity = activity;
        this.trimmedName = trimmedName;
        this.trimmedSurname = trimmedSurname;
        this.phoneNum = phoneNum;
        this.date = bDate;
        this.gender = gender;
        Realm.init(context);

    }




    @Override
    protected Void doInBackground(Void... voids) {


        Realm realm;
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                try {

                    isCreated = true;
                    RealmContact contact = realm.createObject(RealmContact.class, MyFunctions.phoneNumCorrector(phoneNum));
                    contact.setName(trimmedName);
                    contact.setGender(gender);
                    contact.setBdate(date);
                    contact.setSurname(trimmedSurname);
                } catch (Exception ie) {
                    if (ie.getMessage().toLowerCase().contains("primary key")) {
                        isCreated = false;
                    }
                }
            }
        });
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (activity != null) {
            if (isCreated) {
                Toast.makeText(context, "Contact was created.", Toast.LENGTH_SHORT).show();
                activity.finish();
            } else
                Toast.makeText(context, "Contact already exist.", Toast.LENGTH_SHORT).show();
        }


    }
}