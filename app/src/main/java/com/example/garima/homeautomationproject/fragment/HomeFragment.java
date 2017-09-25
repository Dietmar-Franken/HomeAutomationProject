package com.example.garima.homeautomationproject.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.garima.homeautomationproject.R;
import com.example.garima.homeautomationproject.adapter.HomeAdapter;
import com.example.garima.homeautomationproject.adapter.UpdateInterface;
import com.example.garima.homeautomationproject.database.DatabaseHandler;
import com.example.garima.homeautomationproject.model.HomeModel;
import com.example.garima.homeautomationproject.model.RoomModel;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements UpdateInterface {
    private View view;
    private FloatingActionButton addHomeFloatingButton;
    private ArrayList<HomeModel> homeModelArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;

    private DatabaseHandler databaseHandler;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        databaseHandler = new DatabaseHandler(getActivity());
        findViewById();
        setRecyclerView();
        setOnClickListener();
        registerForContextMenu(recyclerView);
        return view;
    }
    private void findViewById() {
        addHomeFloatingButton = view.findViewById(R.id.addHomeFab);
        recyclerView = view.findViewById(R.id.homeListrecyclerView);

    }
    private void setOnClickListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.addHomeFab:
                        openAlertDialog();
                }

            }
        };
        addHomeFloatingButton.setOnClickListener(clickListener);
    }


    private void openAlertDialog() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogLayout = inflater.inflate(R.layout.home_layout, null);
        final AlertDialog builder = new AlertDialog.Builder(getContext()).create();
        builder.setView(dialogLayout);
        builder.setView(dialogLayout);
        builder.setIcon(R.drawable.room);

        ImageView nextImageView = (ImageView)dialogLayout.findViewById(R.id.nextImageView);
        ImageView cancelImageView = (ImageView)dialogLayout.findViewById(R.id.cancelImageView);
       final EditText homeNameET = (EditText)dialogLayout.findViewById(R.id.homeEditText);
        if(nextImageView!=null)
        {
            nextImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String homeName = homeNameET.getText().toString();
                    if (TextUtils.isEmpty(homeName)) {
                        return;
                    }

                    if (!databaseHandler.checkHomeNameAlreadyExist(homeName)) {
                        Toast.makeText(getContext(), "This Home Name Already exist", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        HomeModel homeModel = new HomeModel();
                        homeModel.setHomeName(homeName);
                        homeModelArrayList.add(homeModel);
                        databaseHandler.addHome(homeModel);
                    }
                        homeAdapter.update(homeModelArrayList);

                    builder.dismiss();


                }


            });

        }
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });



        builder.show();



    }

    private void setRecyclerView() {

        homeModelArrayList =  databaseHandler.getHomeList();
        homeAdapter = new HomeAdapter(getContext(), homeModelArrayList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(homeAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        // setRecyclerView();
    }




    @Override
    public void updateList(ArrayList<HomeModel> roomModelArrayList) {
        homeModelArrayList = roomModelArrayList;

    }
}
