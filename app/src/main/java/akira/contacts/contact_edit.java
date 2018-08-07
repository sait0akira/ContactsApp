package akira.contacts;


import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import android.provider.ContactsContract;

import java.util.Calendar;

public class contact_edit extends Fragment {

    @BindView(R.id.nameAdd)
    EditText add_name;

    @BindView(R.id.surnameAdd)
    EditText add_surname;

    @BindView(R.id.phonenumAdd)
    EditText add_phonenum;

    @BindView(R.id.bdateAdd)
    EditText add_bdate;

    @BindView(R.id.genderAdd)
    EditText add_gender;

    private Realm realm;

    @Override
    public void onCreate(final Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);

        Context context = this.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.contact_add, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // @Override
//    public void onDestroy() {
//        super.onDestroy();
//        realm.close();
//    }

    class AsyncDataLoad extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Context thisContext = getContext();
            realm = Realm.getInstance(thisContext);
            realm.beginTransaction();
            contact_shelf contact = realm.createObject(contact_shelf.class);
            contact.setName(getTrimmedName());
            contact.setSurname(getTrimmedSurname());
            contact.setPhoneNum(getTrimmedPhonenum());
            contact.setBdate(getTrimmedBdate());
            contact.setGender(getTrimmedGender());

            realm.commitTransaction();
            realm.close();
            return null;
        }
    }

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener BDATE = new DatePickerDialog.OnDateSetListener(){


        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            myCalendar.set(Calendar.YEAR,year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
add_bdate.setText(myCalendar.get(Calendar.DAY_OF_MONTH)+"/"+myCalendar.get(Calendar.MONTH)+"/"+myCalendar.get(Calendar.YEAR));
        }
    };

    @OnClick(R.id.bdateAdd)
    public void dd(){
        new DatePickerDialog(this.getContext(),BDATE, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    @OnClick(R.id.add_contact)
    public void onAddClick() {

        new AsyncDataLoad().execute();
        Toast.makeText(getContext(), "contact created", Toast.LENGTH_SHORT).show();
        this.getActivity().finish();
    }
    private void updateLabel(){

    }

    @OnClick(R.id.button_remove)
    public void onDelClick() {
        realm = Realm.getInstance(this.getContext());
        realm.beginTransaction();
        contact_shelf contact = realm.createObject(contact_shelf.class);

        RealmResults<contact_shelf> toDel;
        toDel = realm.allObjects(contact_shelf.class);
        int sizee = toDel.size();
        for (int i = sizee - 1; i > -1; i--)
            toDel.get(i).removeFromRealm();
        realm.commitTransaction();
        this.getActivity().finish();
    }

    private String getTrimmedName() {
        if(add_name.getText() != null)
        return add_name.getText().toString().trim();
        else return "";
    }

    private String getTrimmedSurname() {
        if(add_surname.getText() != null)
        return add_surname.getText().toString().trim();
        else return "";
    }

    private String getTrimmedPhonenum() {
        if(add_phonenum != null)
        return add_phonenum.getText().toString().trim();
    else return "";
    }

    private String getTrimmedBdate() {
    if(add_bdate != null)
        return add_bdate.getText().toString();
    else return "";
    }

    private String getTrimmedGender() {
    if(add_gender != null)
        return add_gender.getText().toString().trim();
    else return "";
    }
}
