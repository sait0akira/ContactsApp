package akira.contacts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.Date;


class ContactLoader extends AsyncTask<Void, Void, Void> {
    private Context context;

    ContactLoader(Context context) {
        this.context = context;
    }


    private Cursor getContactsBirthday() {
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

        return context.getContentResolver().query(uri, projection, where, selectionArgs, sortOrder);

    }


    private Cursor getGender() {
        Uri uri = ContactsContract.Data.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Note.NOTE

        };

        String where = null;

        String[] selectionArgs = null;
        String sortOrder = null;
        return context.getContentResolver().query(uri, projection, where, selectionArgs, sortOrder);
    }

    protected Cursor getAll() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        return context.getContentResolver().query(uri, null, null, null, null);
    }


    @Override
    protected Void doInBackground(Void... voids) {

        final Cursor cursor1 = getAll();
        final Cursor getGender = getGender();
        final Cursor getBDATE = getContactsBirthday();
        getBDATE.moveToFirst();
        cursor1.moveToFirst();

        do {


            String tempName = "";
            String tempSurname = "";
            String tempPhoneNum = "";
            Date tempDate = null;
            String tempGender = "";
            String tempContactNumber = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            tempPhoneNum = MyFunctions.phoneNumCorrector(tempContactNumber);


            //  Вбивание имени и фамилии во временную переменную
            String[] ns;
            String altName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE));
            ns = altName.split(",");
            if (altName.contains("+")) {
            } else {
                if (altName.contains(",")) {
                    tempName = (ns[1].trim());
                    tempSurname = (ns[0].trim());
                } else tempName = (ns[0].trim());

            }

            //  Вбивание даты рождения


            getBDATE.moveToFirst();
            do {
                if (getBDATE.getString(0).equals(cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))) {

                    String[] dateSplit = getBDATE.getString(2).split("-");
                    tempDate = (new Date(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2])));

                }

            } while (getBDATE.moveToNext());

            // Вбивание пола

            getGender.moveToFirst();
            do {
                if (getGender.getString(0).equals(cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))) {
                    if ((getGender.getString(2) != null) && (getGender.getString(2).toLowerCase().contains("male")))
                        tempGender = (getGender.getString(2));


                }
            } while (getGender.moveToNext());


            new AsyncManualContactUpload(context, null, tempName, tempSurname, tempPhoneNum, tempDate, tempGender).execute();


        } while (cursor1.moveToNext());

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, "Contacts Loaded", Toast.LENGTH_SHORT).show();
    }
}