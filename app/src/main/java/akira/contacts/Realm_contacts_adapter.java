package akira.contacts;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

public class Realm_contacts_adapter extends RealmRecyclerViewAdapter<Realm_contact, Realm_contacts_adapter.ViewHolder> {

    private final RealmResults<Realm_contact> MyCont;
    Realm realm;


    public Realm_contacts_adapter(OrderedRealmCollection<Realm_contact> Cont) {
        super(Cont, true);
        MyCont = Cont.where().findAll().sort("name", Sort.ASCENDING, "surname", Sort.ASCENDING);
        realm = Realm.getDefaultInstance();

        MyCont.addChangeListener(new RealmChangeListener<RealmResults<Realm_contact>>() {
            @Override
            public void onChange(RealmResults<Realm_contact> contact_shelves) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_form, parent, false);
        return new ViewHolder((CardView) view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Realm_contact realmContact = getItem(position);
        if (realmContact != null) holder.bind(realmContact);

        holder.delc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        MyCont.get(holder.getAdapterPosition()).deleteFromRealm();
                    }
                });
            }

        });

    }

    @Override
    public int getItemCount() {
        return MyCont.size();
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

        public void bind(Realm_contact realmContact) {
            con_name.setText(MyCont.get(getAdapterPosition()).getName());
            con_surname.setText(MyCont.get(getAdapterPosition()).getSurname());
            con_phonenum.setText(MyCont.get(getAdapterPosition()).getPhoneNum());

            try {
                con_bdate.setText(MyCont.get(getAdapterPosition()).getBdate().getDate() + " / " + MyCont.get(getAdapterPosition()).getBdate().getMonth() + " / " + MyCont.get(getAdapterPosition()).getBdate().getYear());
            } catch (NullPointerException ie) {
                con_bdate.setText("");
            }

            con_gender.setText(MyCont.get(getAdapterPosition()).getGender());

        }


    }
}
