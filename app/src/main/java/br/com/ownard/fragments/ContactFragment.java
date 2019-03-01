package br.com.ownard.fragments;


import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.ownard.adapters.ContactAdapterRV;
import br.com.ownard.forgetme.R;
import br.com.ownard.models.Contact;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACT = 11;
    private RecyclerView mRecyclerView;
    private ContactAdapterRV adapterRV;
    private List<Contact> mContacts;
    LinearLayoutManager llm = null;

    private static int limit = 10;
    private static int count = 0;


    public ContactFragment() {
        // Required empty public constructor
    }

    public void initializeItems() {
        mContacts = mapContacts();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContacts = mapContacts();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_contact_list);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                ContactAdapterRV adapterRV = (ContactAdapterRV) recyclerView.getAdapter();

                if (mContacts.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    ContactFragment.count += 10;
                    List<Contact> listAux = mapContacts();

                    for (int i = 0; i < listAux.size(); i++) {
                        adapterRV.addListItem(listAux.get(i), mContacts.size());
                    }

                }

            }


        });



        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);


        adapterRV = new ContactAdapterRV(mContacts, R.layout.recyclerview_contact_list, getActivity());
        mRecyclerView.setAdapter(adapterRV);

        return view;
    }


    private List<Contact> mapContacts() {
        ContentResolver ct = getActivity().getContentResolver();

        Cursor cursor = ct.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC LIMIT " + count + "," + limit);

        List<Contact> contatosAuxes = new ArrayList<>();

        int controlLimit = 0;

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String phone = String.valueOf(R.string.undefined);
                String photo = null;
                if ((cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorPhones = ct.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (cursorPhones.moveToNext()) {
                        phone = cursorPhones.getString(cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                }

                photo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                Log.d("Thumb", String.valueOf(photo));

                Contact contact = new Contact(contactName, phone, 30);
                if (photo != null) {
                    contact.setPhoto(loadContactPhotoThumbnail(photo));
                } else {
                    Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.drawable.ic_person_black_36dp);
                    contact.setPhoto(icon);
                }
                contatosAuxes.add(contact);
            }
            cursor.close();
        }

        Log.d("mide", String.valueOf(limit));

        return contatosAuxes;

    }


    private Bitmap loadContactPhotoThumbnail(String photoData) {
        // Creates an asset file descriptor for the thumbnail file.
        AssetFileDescriptor afd = null;
        // try-catch block for file not found
        try {
            // Creates a holder for the URI.
            Uri thumbUri;
            // If Android 3.0 or later
            if (Build.VERSION.SDK_INT
                    >=
                    Build.VERSION_CODES.HONEYCOMB) {
                // Sets the URI from the incoming PHOTO_THUMBNAIL_URI
                thumbUri = Uri.parse(photoData);
            } else {
                // Prior to Android 3.0, constructs a photo Uri using _ID
                /*
                 * Creates a contact URI from the Contacts content URI
                 * incoming photoData (_ID)
                 */
                final Uri contactUri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_URI, photoData);
                /*
                 * Creates a photo URI by appending the content URI of
                 * Contacts.Photo.
                 */
                thumbUri =
                        Uri.withAppendedPath(
                                contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            }

        /*
         * Retrieves an AssetFileDescriptor object for the thumbnail
         * URI
         * using ContentResolver.openAssetFileDescriptor
         */
            afd = getActivity().getContentResolver().
                    openAssetFileDescriptor(thumbUri, "r");
        /*
         * Gets a file descriptor from the asset file descriptor.
         * This object can be used across processes.
         */
            FileDescriptor fileDescriptor = afd.getFileDescriptor();
            // Decode the photo file and return the result as a Bitmap
            // If the file descriptor is valid
            if (fileDescriptor != null) {
                // Decodes the bitmap
                return BitmapFactory.decodeFileDescriptor(
                        fileDescriptor, null, null);
            }
            // If the file isn't found
        } catch (FileNotFoundException e) {
            /*
             * Handle file not found errors
             */
            // In all cases, close the asset file descriptor
        } finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public void update() {
        if (mContacts != null) {
            adapterRV.notifyData(mContacts);
        }
    }

}
