package akira.contacts;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class Recycler_contacts_list extends Fragment {

    @BindView(R.id.recView)
    RecyclerView rcv;
    Realm realm;

    @Override
    public void onCreate(final Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.rcv, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv.setAdapter(new Realm_contacts_adapter(realm.where(Realm_contact.class).findAllAsync()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}

