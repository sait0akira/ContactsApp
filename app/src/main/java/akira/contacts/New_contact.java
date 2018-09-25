package akira.contacts;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class New_contact extends AppCompatActivity {
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.contact_add_fragment);
        if (savedInstantState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.ContactCreation, new New_contact_fragment())

                    .commit();

        }
    }

    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Canceled.", Toast.LENGTH_SHORT).show();
        finish();
    }

}








