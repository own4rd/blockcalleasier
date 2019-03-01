package br.com.ownard.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.ownard.adapters.BlockedCallAdapterRV;
import br.com.ownard.activities.MainActivity;
import br.com.ownard.forgetme.R;
import br.com.ownard.models.Contact;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlockCallsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TextView emptyView;
    private BlockedCallAdapterRV adapterRV;

    public BlockCallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_block_calls, container, false);

        mRecyclerView = view.findViewById(R.id.rvContact);
        emptyView = view.findViewById(R.id.is_empty);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        adapterRV = new BlockedCallAdapterRV(getBlockCalls(), R.layout.recyvlerview_blocked_contato, getActivity());
        mRecyclerView.setAdapter(adapterRV);

        this.changeView();


        return view;
    }

    private List<Contact> getBlockCalls(){
        MainActivity mainActivity = (MainActivity)getActivity();
        return mainActivity.getCallController().load();
    }

    public void changeView(){
        if (getBlockCalls().isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }else{
            emptyView.setVisibility(View.GONE);
        }
    }

    public void search(String phone){
        MainActivity mainActivity = (MainActivity)getActivity();
        adapterRV.notifyData(mainActivity.getCallController().search(phone));
    }
    public void update(){
        this.changeView();
        adapterRV.notifyData(getBlockCalls());
    }

}
