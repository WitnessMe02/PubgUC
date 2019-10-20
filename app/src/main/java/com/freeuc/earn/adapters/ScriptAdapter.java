package com.freeuc.earn.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.freeuc.earn.R;
import com.freeuc.earn.listeners.OnScriptItemClickListener;
import com.freeuc.earn.models.Script;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Script} and makes a call to the
 * specified {@link OnScriptItemClickListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ScriptAdapter extends RecyclerView.Adapter<ScriptAdapter.ViewHolder> {

    private final List<Script> scripts;
    private final OnScriptItemClickListener mListener;

    public ScriptAdapter(List<Script> items, OnScriptItemClickListener listener) {
        scripts = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_script, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bind(scripts.get(position),mListener);
    }

    @Override
    public int getItemCount() {
        return scripts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView title;
        private final TextView amount;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.title);
            amount = view.findViewById(R.id.amount);
        }
        public void bind(final Script script, OnScriptItemClickListener listener){
            title.setText(script.getTitle());
            amount.setText("\u20B9 "+String.valueOf(script.getAmount()));
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onClick(script);
                    }
                }
            });
        }
    }
}
