package ru.maximumdance.passcontrol.listadapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderImpl extends RecyclerView.ViewHolder {

    View view;

    ViewHolderImpl(View v) {
        super(v);
        this.view = v;

    }
}