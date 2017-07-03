package alee.info.addressbook.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alee.info.addressbook.Extras.Utilities;
import alee.info.addressbook.Pojo.RandomUsers;
import alee.info.addressbook.R;


public class SimpleAdapter extends
        RecyclerView.Adapter<SimpleAdapter.MyViewHolder> {

    private List<RandomUsers> list_item;
    public Context mcontext;


    public SimpleAdapter(List<RandomUsers> list, Context context) {

        list_item = list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public SimpleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.temp_list, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
        viewHolder.setIsRecyclable(false);
        viewHolder.name.setText(list_item.get(position).getName().toUpperCase());
        viewHolder.email.setText(list_item.get(position).getEmail());
        viewHolder.contact.setText(list_item.get(position).getPhone());
        if (position == 0) {
            viewHolder.img.setImageBitmap(list_item.get(0).getPicture());
        }


        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.makeCall(list_item.get(position).getPhone(), mcontext);
            }
        });

        viewHolder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.sendEmail(list_item.get(position).getEmail(), mcontext);
            }
        });
    }


    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, email, contact;
        public ImageView img, call, mail;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = (TextView) itemLayoutView.findViewById(R.id.name);
            call = (ImageView) itemLayoutView.findViewById(R.id.call);
            mail = (ImageView) itemLayoutView.findViewById(R.id.mail);
            contact = (TextView) itemLayoutView.findViewById(R.id.contact);
            email = (TextView) itemLayoutView.findViewById(R.id.email);
            img = (ImageView) itemLayoutView.findViewById(R.id.c_img);

        }

    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return list_item.size();
    }
}
