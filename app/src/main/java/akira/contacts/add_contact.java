package akira.contacts;

import android.app.Application;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class add_contact extends AppCompatActivity {
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.contact_add_fragment);
        if (savedInstantState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.ContactCreation, new contact_edit())

                    .commit();

        }
    }




}

