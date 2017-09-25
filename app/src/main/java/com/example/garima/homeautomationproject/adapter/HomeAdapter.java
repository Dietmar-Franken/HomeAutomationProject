package com.example.garima.homeautomationproject.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garima.homeautomationproject.R;
import com.example.garima.homeautomationproject.activity.RoomActivity;
import com.example.garima.homeautomationproject.database.DatabaseHandler;
import com.example.garima.homeautomationproject.model.HomeModel;
import com.example.garima.homeautomationproject.model.RoomModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.garima.homeautomationproject.constant.Constant.HOME_MODEL;

/**
 * Created by GARIMA on 9/20/2017.
 */

public class HomeAdapter extends  RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<HomeModel> HomeModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private LinearLayout roomDescriptionLL;
    private LinearLayout menuLinearLayout;
    private HomeModel homeModel;

    private DatabaseHandler databaseHandler;
    private UpdateInterface updateInterface;




    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        databaseHandler = new DatabaseHandler(context);
        View itemView = layoutInflater.inflate(R.layout.room_row_item, parent, false);
        HomeAdapter.HomeViewHolder homeViewHolder = new HomeAdapter.HomeViewHolder(itemView);
        return homeViewHolder;
    }

    public HomeAdapter(Context context, List<HomeModel> homeModelList,UpdateInterface updateInterface ) {
        this.updateInterface=updateInterface;
        this.HomeModelList = homeModelList;
        this.context = context;

    }

    @Override
    public void onBindViewHolder(HomeAdapter.HomeViewHolder holder, int position) {
        if (HomeModelList != null && HomeModelList.size() > position) {
            homeModel = HomeModelList.get(position);
            if (homeModel != null) {
                holder.roomNameTextView.setText(homeModel.getHomeName());

            }
            setClickListener(holder.menuLinearLayout, homeModel, position);
            setClickListener(holder.roomDescriptionLinearLayout, homeModel, position);
        }

    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {
        private TextView roomNameTextView;
        private TextView roomMenuTextView;
        private LinearLayout menuLinearLayout;
        private LinearLayout roomDescriptionLinearLayout;


        public HomeViewHolder(View view) {
            super(view);
            roomNameTextView = (TextView) view.findViewById(R.id.roomNameTextView);
            roomMenuTextView = (TextView) view.findViewById(R.id.roomMenuTextView);
            menuLinearLayout = (LinearLayout) view.findViewById(R.id.menuLinearLayout);
            roomDescriptionLinearLayout = (LinearLayout) view.findViewById(R.id.roomDiscriptionLL);


        }


    }

    @Override
    public int getItemCount() {

        if (HomeModelList == null) {
            return 0;
        }
        updateInterface.updateList((ArrayList<HomeModel>) HomeModelList);
        return HomeModelList.size();
    }

    public void update(ArrayList<HomeModel> datas) {
        HomeModelList = datas;
        notifyDataSetChanged();
    }

    private void setClickListener(final LinearLayout linearLayout, final HomeModel homeModel, final int position) {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.menuLinearLayout: {
                        PopupMenu popup = new PopupMenu(context, linearLayout);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.options_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.edit:
                                        String prevHomeName = HomeModelList.get(position).getHomeName();
                                        showEditDialog(prevHomeName);
                                        break;
                                    case R.id.delete:
                                        String homeName = HomeModelList.get(position).getHomeName();
                                        showDeleteDialog(homeName);
                                        break;

                                }
                                return false;
                            }
                        });
                        //displaying the popup
                        popup.show();
                    }
                    break;
                    case R.id.roomDiscriptionLL: {
                        Intent intent = new Intent(context, RoomActivity.class);
                        intent.putExtra(HOME_MODEL, homeModel);
                        context.startActivity(intent);
                    }
                    break;

                }

            }
        });
    }

    private void showEditDialog(final String prevHomeName) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Edit");
        alertDialog.setMessage("Change Home Name");


        final EditText deviceNameEditText = new EditText(context);
        deviceNameEditText.setSingleLine();
        deviceNameEditText.setText(prevHomeName);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(deviceNameEditText);
        alertDialog.setView(layout);

        alertDialog.setIcon(R.drawable.room);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String newHomeName = deviceNameEditText.getText().toString();

                        if (TextUtils.isEmpty(newHomeName)) {
                            return;
                        }


                        if (!databaseHandler.checkHomeNameAlreadyExist(newHomeName)) {
                            Toast.makeText(context, "This Home Name Already exist", Toast.LENGTH_LONG).show();
                            return;
                        } else {


                            int result = databaseHandler.updateHomeName(prevHomeName, newHomeName);
                            if (result == 1) {
                                HomeModelList = databaseHandler.getHomeList();
                                updateInterface.updateList((ArrayList<HomeModel>)HomeModelList);
                                update((ArrayList<HomeModel>) HomeModelList);

                            }
                        }


                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void showDeleteDialog(final String homeName) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Are You Sure ,Do You want to delete this Home");
        alertDialog.setIcon(R.drawable.room);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHandler.deleteHome(homeName);
                        HomeModelList = databaseHandler.getHomeList();
                        update((ArrayList<HomeModel>) HomeModelList);


                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


}

