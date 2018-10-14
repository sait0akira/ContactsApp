package akira.contacts;


import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class RealmContactsAdapter extends RealmRecyclerViewAdapter<RealmContact, RealmContactsAdapter.ViewHolder> {


    Realm realm;


    RealmContactsAdapter(OrderedRealmCollection<RealmContact> Cont, boolean autoUpdate) {
        super(Cont, true);
        realm = Realm.getDefaultInstance();


    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_form, parent, false);
        return new ViewHolder((CardView) view);
    }

    @Nullable
    @Override
    public OrderedRealmCollection<RealmContact> getData() {

        return super.getData();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        RealmContact realmContact = getItem(position);
        if (realmContact != null) holder.bind(realmContact);

        holder.delc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.delc.setClickable(false);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        getData().get(holder.getAdapterPosition()).deleteFromRealm();
                    }
                });
            }

        });

    }

    @Override
    public int getItemCount() {
        try {
            return getData().size();
        } catch (NullPointerException ie) {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView con_name;
        private TextView con_surname;
        private TextView con_phonenum;
        private TextView con_bdate;
        private TextView con_gender;
        public View Card;
        Button delc;


        public ViewHolder(final CardView itemView) {
            super(itemView);
            con_name = itemView.findViewById(R.id.ConName);
            con_surname = itemView.findViewById(R.id.ConSurname);
            con_phonenum = itemView.findViewById(R.id.ConPhone);
            con_bdate = itemView.findViewById(R.id.ConBdate);
            con_gender = itemView.findViewById(R.id.ConGender);
            Card = itemView.findViewById(R.id.ContactCard);
            delc = itemView.findViewById(R.id.delete_contactt);


        }

        public void bind(RealmContact realmContact) {
            con_name.setText(getData().get(getAdapterPosition()).getName());
            con_surname.setText(getData().get(getAdapterPosition()).getSurname());
            con_phonenum.setText(getData().get(getAdapterPosition()).getPhoneNum());
            con_gender.setText(getData().get(getAdapterPosition()).getGender());
            try {
                con_bdate.setText(getData().get(getAdapterPosition()).getBdate().getDate() + " / " + getData().get(getAdapterPosition()).getBdate().getMonth() + " / " + getData().get(getAdapterPosition()).getBdate().getYear());
            } catch (NullPointerException ie) {
                con_bdate.setText("No date");
            }


        }


    }
}
