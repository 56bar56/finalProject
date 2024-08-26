package com.example.finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.items.MessageToGet;

import java.util.List;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView message;
        private final TextView hourSent;

        private MessageViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message); //Or message in
            hourSent = itemView.findViewById(R.id.hourSent);
        }
    }

    private final LayoutInflater mInflater;
    private List<MessageToGet> messsages;
    private String username;

    public MessageListAdapter(Context context, String username) {
        mInflater = LayoutInflater.from(context);
        this.username = username;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.message_layout, parent, false);
        return new MessageViewHolder(itemView);
    }
    public String fixDate(String date) {
        String inputDateString = date;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("HH:mm dd.MM.yy", Locale.getDefault());

        try {
            Date inputDate = inputDateFormat.parse(inputDateString);
            String cleanedDateString = outputDateFormat.format(inputDate);
            return cleanedDateString;
        } catch (ParseException e) {
            return null;
        }
    }
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        if (messsages != null) {
            final MessageToGet current = messsages.get(position);
            holder.message.setText(current.getContent());
            String date = fixDate(current.getCreated());
            if(date != null)
                holder.hourSent.setText(date);
            else
                holder.hourSent.setText(current.getCreated());

            if (current.getSender().getUsername().equals(this.username)) { // TODO change to your username
                holder.message.setBackgroundResource(R.drawable.message_out_bubble);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.message.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.setMargins(200, 4, 30, 8);
                holder.message.setLayoutParams(params);

                RelativeLayout.LayoutParams hourParams = (RelativeLayout.LayoutParams) holder.hourSent.getLayoutParams();
                hourParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                hourParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                hourParams.addRule(RelativeLayout.ALIGN_TOP, holder.message.getId());
                holder.hourSent.setLayoutParams(hourParams);
            } else {
                holder.message.setBackgroundResource(R.drawable.message_in_bubble);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.message.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.setMargins(30, 4, 200, 8);
                holder.message.setLayoutParams(params);

                RelativeLayout.LayoutParams hourParams = (RelativeLayout.LayoutParams) holder.hourSent.getLayoutParams();
                hourParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                hourParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                hourParams.addRule(RelativeLayout.ALIGN_TOP, holder.message.getId());
                holder.hourSent.setLayoutParams(hourParams);
            }
        }
    }


    public void setMessages(List<MessageToGet> m) {
        messsages = m;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (messsages != null)
            return messsages.size();
        else return 0;
    }

    public List<MessageToGet> getMessages() {
        return messsages;
    }

}

