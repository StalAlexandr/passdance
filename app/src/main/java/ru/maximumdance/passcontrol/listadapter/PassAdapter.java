package ru.maximumdance.passcontrol.listadapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.maximumdance.passcontrol.R;
import ru.maximumdance.passcontrol.model.Pass;

public class PassAdapter extends RecyclerView.Adapter<ViewHolderImpl> {


    private final List<Pass> passes;

    PassClickListener listener;

    public PassAdapter(List<Pass> passes, PassClickListener listener) {
        this.passes = passes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.passlist, parent, false);
        return new ViewHolderImpl(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl holder, int position) {

        holder.view.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
        TextView txtTitle =  holder.view.findViewById(R.id.passName);
        ImageView editView =  holder.view.findViewById(R.id.icoEdit);
        txtTitle.setText(passes.get(position).toString() );

        txtTitle.setOnClickListener(view -> listener.click(position, PassEvent.ADDLESSON));

        editView.setOnClickListener(view -> listener.click(position, PassEvent.UPDATE));

    }

    @Override
    public int getItemCount() {
        return passes.size();
    }


    public  interface PassClickListener{
        void click(int position, PassEvent event);
    }


    public enum PassEvent{
        ADDLESSON, UPDATE, DELETE;
    }
}
