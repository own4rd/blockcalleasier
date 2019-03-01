package br.com.ownard.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.ownard.activities.MainActivity;
import br.com.ownard.forgetme.R;
import br.com.ownard.models.Contact;


public class BlockedCallAdapterRV extends RecyclerView.Adapter<BlockedCallAdapterRV.ContactViewHolder> {

    private List<Contact> mContacts;
    private int layoutInflate;
    Activity currentActivity;

    public BlockedCallAdapterRV(List<Contact> contacts, int layout, Activity activity) {
        this.mContacts = contacts;
        this.layoutInflate = layout;
        this.currentActivity = activity;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutInflate, parent, false);
        ContactViewHolder contatoVH = new ContactViewHolder(v);
        return contatoVH;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {

        holder.tvName.setText(mContacts.get(position).getName());
        holder.tvNumber.setText(mContacts.get(position).getPhoneNumber());
        holder.tvNumberCallBlock.setText(String.valueOf(mContacts.get(position).getBlockCalls()));

        holder.btnDeleteCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)currentActivity).getCallController().delete(mContacts.get(position).getPhoneNumber());
                Toast.makeText(currentActivity, mContacts.get(position).getName() + R.string.number_removed, Toast.LENGTH_SHORT).show();
                mContacts.remove(mContacts.get(position));

                ((MainActivity)currentActivity).notifyRVChange();
                ((MainActivity)currentActivity).notifyRVItemRemoved();
            }
        });

    }

    public void notifyData(List<Contact> contacts){
        this.mContacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName;
        public TextView tvNumber;
        public TextView isEmpty;
        public TextView tvNumberCallBlock;
        public ImageButton btnDeleteCall;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView)itemView.findViewById(R.id.tv_block_name);
            tvNumber = (TextView)itemView.findViewById(R.id.tv_block_number);
            isEmpty = (TextView)itemView.findViewById(R.id.is_empty);
            tvNumberCallBlock = (TextView)itemView.findViewById(R.id.tv_count_call_block);
            btnDeleteCall = (ImageButton)itemView.findViewById(R.id.ic_aux);
        }
    }
}
