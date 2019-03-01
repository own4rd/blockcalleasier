package br.com.ownard.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import br.com.ownard.forgetme.R;


public class DialogNewBlock extends DialogFragment {

    private EditText mEditTextName;
    private EditText mEditTextPhone;

    public DialogNewBlock() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_newblock,null));

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_newblock, container);
        mEditTextName = (EditText)view.findViewById(R.id.new_name);
        mEditTextPhone = (EditText)view.findViewById(R.id.new_phone);


        return view;
    }
}
