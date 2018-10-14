package akira.contacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public Context context;
    public Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        context = this.getApplicationContext();

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Realm.init(getApplicationContext());

        new ContactLoader(getApplicationContext()).execute();


        if (savedInstanceState == null)

        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.viewFrame, new RecyclerContactsList())
                    .commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    public boolean OnClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.new_contact) {
            Intent intent = new Intent(MainActivity.this, NewContact.class);
            startActivity(intent);

        }

        if (menuItem.getItemId() == R.id.UploadCSV) {
            new CSVAsync(getApplicationContext()).execute();


        }

        return true;
    }


    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        final AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Выйти из приложения?");
        quitDialog.setPositiveButton("Выход", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        quitDialog.show();
    }


}