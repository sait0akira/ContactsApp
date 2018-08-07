package akira.contacts;


import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import android.widget.Toast;
import android.provider.ContactsContract;


public class contact_adapter extends RecyclerView.Adapter<contact_adapter.ViewHolder> implements RealmChangeListener {

    private final RealmResults<contact_shelf> MyCont;
    private RealmResults<contact_shelf> DelCont;
    private android.content.Context thisContext;
    Realm realm;


    public contact_adapter(RealmResults<contact_shelf> Cont) {
        MyCont = Cont.where().findAllSorted("name", Sort.ASCENDING,"surname",Sort.ASCENDING);


        MyCont.addChangeListener(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_form, parent, false);
        return new ViewHolder((CardView) view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.bind(holder);
        holder.delc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.beginTransaction();
                //Toast.makeText(thisContext, "Contact " + MyCont.get(position).getName() + " was deleted", Toast.LENGTH_SHORT).show();
                MyCont.get(position).removeFromRealm();
                realm.commitTransaction();
            }

        });

    }

    @Override
    public int getItemCount() {
        return MyCont.size();
    }

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private TextView con_name;
        private TextView con_surname;
        private TextView con_phonenum;
        private TextView con_bdate;
        private TextView con_gender;
        public View Card;
Button delc;
        int ColorBack;


        public ViewHolder(final CardView itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            con_name = itemView.findViewById(R.id.ConName);
            con_surname = itemView.findViewById(R.id.ConSurname);
            con_phonenum = itemView.findViewById(R.id.ConPhone);
            con_bdate = itemView.findViewById(R.id.ConBdate);
            con_gender = itemView.findViewById(R.id.ConGender);
            ColorBack = Color.parseColor("#ffffff");
            Card = itemView.findViewById(R.id.ContactCard);
            thisContext = itemView.getContext();
            realm = Realm.getInstance(thisContext);
            delc = itemView.findViewById(R.id.delete_contactt);


        }

        public void bind(ViewHolder holder) {
            con_name.setText(MyCont.get(getAdapterPosition()).getName());
con_surname.setText(MyCont.get(getAdapterPosition()).getSurname());
con_phonenum.setText(MyCont.get(getAdapterPosition()).getPhoneNum());
con_bdate.setText(MyCont.get(getAdapterPosition()).getBdate());
con_gender.setText(MyCont.get(getAdapterPosition()).getGender());

        }

        @Override
        public boolean onLongClick(View view) {
           // Card.setBackgroundColor(ColorBack);
            return true;
        }
    }
}
