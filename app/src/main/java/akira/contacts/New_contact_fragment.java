package akira.contacts;


import android.app.DatePickerDialog;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.Calendar;
import java.util.Date;

public class New_contact_fragment extends Fragment {

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
    Date temp_data;


    @Override
    public void onCreate(final Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.contact_add, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    class AsyncDataLoad extends AsyncTask<Void, Void, Void> {
        private boolean isCreated = true;

        @Override
        protected Void doInBackground(Void... voids) {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        Realm_contact contact = realm.createObject(Realm_contact.class, new MyFunctions().phoneNumCorrector(add_phonenum.getText().toString()));
                        isCreated = true;
                        contact.setName(getTrimmedName());
                        contact.setSurname(getTrimmedSurname());

                        contact.setBdate(new Date(temp_data.getYear(), temp_data.getMonth(), temp_data.getDate()));

                        contact.setGender(getTrimmedGender());

                    } catch (Exception ie) {
                        if (ie.getMessage().toLowerCase().contains("primary key"))
                            isCreated = false;
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isCreated) {
                Toast.makeText(getContext(), "Contact was created.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else
                Toast.makeText(getContext(), "Contact already exist.", Toast.LENGTH_SHORT).show();
        }
    }

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener BDATE = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            temp_data = new Date(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH) + 1, myCalendar.get(Calendar.DAY_OF_MONTH));
            add_bdate.setText(temp_data.getDate() + "/" + temp_data.getMonth() + "/" + temp_data.getYear());
        }
    };

    @OnClick(R.id.bdateAdd)
    public void dd() {
        new DatePickerDialog(getContext(), BDATE, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    @OnClick(R.id.add_contact)
    public void onAddClick() {

        if (add_phonenum.getText().toString().length() != 0) {
            new AsyncDataLoad().execute();
        } else {
            Toast.makeText(getContext(), "Please enter phone number.", Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.button_remove)
    public void onDelClick() {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Realm_contact> toDel = realm.where(Realm_contact.class).findAll();
                toDel.deleteAllFromRealm();
            }
        });

        this.getActivity().finish();
    }

    private String getTrimmedName() {
        if (add_name.getText() != null)
            return add_name.getText().toString().trim();
        else return "";
    }

    private String getTrimmedSurname() {
        if (add_surname.getText() != null)
            return add_surname.getText().toString().trim();
        else return "";
    }

    private String getTrimmedGender() {
        if (add_gender != null)
            return add_gender.getText().toString().trim();
        else return "";
    }

}

