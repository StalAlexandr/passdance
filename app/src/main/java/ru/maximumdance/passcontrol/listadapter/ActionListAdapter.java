package ru.maximumdance.passcontrol.listadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.maximumdance.passcontrol.Action;
import ru.maximumdance.passcontrol.R;

public class ActionListAdapter extends RecyclerView.Adapter<ActionListAdapter.MyViewHolder> {

    private final List<Action> actions;

    public ActionListAdapter(List<Action> actions) {
        this.actions = actions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menulist, parent, false);
        return new ActionListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.view.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
        TextView txtTitle =  holder.view.findViewById(R.id.item);
        ImageView imageView =  holder.view.findViewById(R.id.icon);
        txtTitle.setText(actions.get(position).getName());
        imageView.setImageResource(actions.get(position).getImageId());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.get(position).getCall().call();
            }
        });

    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        View view;
        MyViewHolder(View v) {
            super(v);
            this.view = v;

        }
    }
}