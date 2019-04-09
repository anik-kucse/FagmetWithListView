package my.demo.fagmetwithlistview;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListHolder extends Fragment {

    public ListHolder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list_holder, container, false);

        final UserRoomDatabase userRoomDatabase = UserRoomDatabase.getUserRoomDatabase(getActivity().getApplicationContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        final CustomAdapter customAdapter = new CustomAdapter(view.getContext());
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                customAdapter.addUser(userRoomDatabase.userDao().getAllUser());
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                customAdapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setTitle("Add New User");
                dialog.setContentView(R.layout.new_user);
                dialog.show();

                final EditText dia_name = dialog.findViewById(R.id.textViewDialogName);
                final EditText dia_age = dialog.findViewById(R.id.textViewDialogAge);
                Button dia_save = dialog.findViewById(R.id.buttonSave);

                dia_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final User user = new User(dia_name.getText().toString(), dia_age.getText().toString());
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                userRoomDatabase.userDao().insert(user);
                                customAdapter.addUser(userRoomDatabase.userDao().getLastUser());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        customAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                        dialog.cancel();
                    }
                });
            }
        });


        return view;
    }

    public static ListHolder getInstance(){
        return new ListHolder();
    }

}