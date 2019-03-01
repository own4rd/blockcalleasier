package br.com.ownard.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.provider.BlockedNumberContract;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.ownard.activities.MainActivity;
import br.com.ownard.forgetme.R;
import br.com.ownard.models.Contact;

public class RecentsAdapterRV extends RecyclerView.Adapter<RecentsAdapterRV.ContatoViewHolder> {

    private List<Contact> mContacts;
    private int layoutInflate;
    private Activity currentActivity;

    public RecentsAdapterRV(List<Contact> mContacts, int layout, Activity activity) {
        this.mContacts = mContacts;
        this.layoutInflate = layout;
        this.currentActivity = activity;
    }

    @Override
    public ContatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutInflate, parent, false);
        ContatoViewHolder contatoVH = new ContatoViewHolder(v);
        return contatoVH;
    }

    @Override
    public void onBindViewHolder(final ContatoViewHolder holder, final int position) {
        holder.tvName.setText(mContacts.get(position).getName());
        holder.tvNumber.setText(mContacts.get(position).getPhoneNumber());
        final ImageButton btnAddBlock = (ImageButton)holder.btnAddBlock;

        if(holder.imgPhoto != null && mContacts.get(position).getPhoto() != null){
            holder.imgPhoto.setImageBitmap(mContacts.get(position).getPhoto());
        }

        btnAddBlock.setBackgroundResource(R.drawable.ic_add_black_36dp);

        if (((MainActivity)currentActivity).getCallController().checkPhone(mContacts.get(position).getPhoneNumber())){
            btnAddBlock.setBackgroundResource(R.drawable.ic_block_black_36dp);
        }



        holder.btnAddBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!((MainActivity)currentActivity).getCallController().checkPhone(mContacts.get(position).getPhoneNumber())){
                    ((MainActivity) currentActivity).getCallController().insert(mContacts.get(position).getName(), mContacts.get(position).getPhoneNumber());
                    Toast.makeText(currentActivity, R.string.add_blocked, Toast.LENGTH_SHORT).show();
                    btnAddBlock.setBackgroundResource(R.drawable.ic_block_black_36dp);
                    ((MainActivity)currentActivity).notifyRVChange();
                }else{
                    Toast.makeText(currentActivity, R.string.already_add, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void checkIfOreo(String phone){
        //TelecomManager telecomManager = (TelecomManager) currentActivity.getSystemService(Context.TELECOM_SERVICE);
        //Context.startActivity(telecomManager.createManageBlockedNumbersIntent(), null);

        ContentValues values = new ContentValues();
        values.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, phone);
        Uri uri = currentActivity.getContentResolver().insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, values);
    }

    public void notifyData(List<Contact> contacts){
        this.mContacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }



    public static class ContatoViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName;
        public TextView tvNumber;
        public ImageView imgPhoto;
        public ImageButton btnAddBlock;

        public ContatoViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView)itemView.findViewById(R.id.tv_nome_contato);
            tvNumber = (TextView)itemView.findViewById(R.id.tv_number_contato);
            imgPhoto = (ImageView)itemView.findViewById(R.id.iv_contact);
            btnAddBlock = (ImageButton)itemView.findViewById(R.id.btn_contact_addblock);

        }
    }
}
