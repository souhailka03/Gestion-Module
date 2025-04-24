package com.ests.gestionmodules.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ests.gestionmodules.R;
import com.ests.gestionmodules.data.entity.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {
    private List<Module> modules = new ArrayList<>();
    private OnModuleClickListener listener;

    public interface OnModuleClickListener {
        void onModuleClick(Module module);
        void onEditClick(Module module);
        void onDeleteClick(Module module);
    }

    public ModuleAdapter(OnModuleClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = modules.get(position);
        holder.moduleName.setText(module.getTitle());
        holder.moduleDescription.setText(module.getDescription());
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
        notifyDataSetChanged();
    }

    class ModuleViewHolder extends RecyclerView.ViewHolder {
        private TextView moduleName;
        private TextView moduleDescription;

        ModuleViewHolder(View itemView) {
            super(itemView);
            moduleName = itemView.findViewById(R.id.moduleName);
            moduleDescription = itemView.findViewById(R.id.moduleDescription);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onModuleClick(modules.get(position));
                }
            });

            itemView.findViewById(R.id.editButton).setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEditClick(modules.get(position));
                }
            });

            itemView.findViewById(R.id.deleteButton).setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(modules.get(position));
                }
            });
        }
    }
} 