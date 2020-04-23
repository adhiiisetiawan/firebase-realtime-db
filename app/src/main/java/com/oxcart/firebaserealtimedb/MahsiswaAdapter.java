package com.oxcart.firebaserealtimedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MahsiswaAdapter extends RecyclerView.Adapter<MahsiswaAdapter.CustomViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<Mahasiswa> mahasiswaArrayList;
    private Context context;

    public MahsiswaAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder,final int position) {
        final String nim = mahasiswaArrayList.get(position).getNim();
        final String nama = mahasiswaArrayList.get(position).getNama();
        holder.textNIM.setText(nim);
        holder.textNama.setText(nama);

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mahasiswaArrayList.get(position).getNim()==holder.textNIM.getText().toString())
                {
                    mahasiswaArrayList.get(position).setNama(holder.textNama.getText().toString());
                    mahasiswaArrayList.get(position).setNim(holder.textNIM.getText().toString());
                    updateitem(mahasiswaArrayList.get(position));
                }
                else{

                    deleteitem(mahasiswaArrayList.get(position).getNim());
                    mahasiswaArrayList.get(position).setNama(holder.textNama.getText().toString());
                    mahasiswaArrayList.get(position).setNim(holder.textNIM.getText().toString());
                    updateitem(mahasiswaArrayList.get(position));

                }
                notifyDataSetChanged();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteitem(mahasiswaArrayList.get(position).getNim());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mahasiswaArrayList.size();
    }

    public void addItem(ArrayList<Mahasiswa> mMahasiswa){
        this.mahasiswaArrayList = mMahasiswa;
        notifyDataSetChanged();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private EditText textNIM;
        private EditText textNama;
        private Button btnUpdate;
        private Button btnDelete;
        private CardView cardView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            textNIM = itemView.findViewById(R.id.edit_nip);
            textNama = itemView.findViewById(R.id.edit_nama);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            cardView = itemView.findViewById(R.id.cv);
        }
    }

    private void updateitem(Mahasiswa mahasiswa) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("mahasiswa").child(mahasiswa.getNim());
        dR.setValue(mahasiswa);
        Toast.makeText(context, "Mahasiswa Updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteitem(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("mahasiswa").child(id);
        dR.removeValue();
        Toast.makeText(context, "Mahasiswa deleted", Toast.LENGTH_SHORT).show();

    }
}
