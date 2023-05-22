package ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampahgo.R;

import java.util.ArrayList;
import model.lokasi;

public class LokasiRecylerAdapter extends RecyclerView.Adapter<LokasiRecylerAdapter.ViewHolder> {
    Context context;
    ArrayList<lokasi> list;
    public LokasiRecylerAdapter(Context context, ArrayList<lokasi> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.lokasi_row, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        lokasi user = list.get(position);
        holder.NamaLokasi.setText(user.getNamaLokasi());
        holder.AlamatLokasi.setText(user.getAlamatLokasi());
        holder.NohpLokasi.setText(user.getNohpLokasi());
        holder.LinkLokasi.setOnClickListener(view -> {
            String url = user.getLinkLokasi();
            Intent x = new Intent(Intent.ACTION_VIEW);
            x.setData(Uri.parse(url));
            context.startActivity(x);
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView NamaLokasi;
        TextView AlamatLokasi;
        TextView NohpLokasi;
        TextView LinkLokasi;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            NamaLokasi = itemView.findViewById(R.id.NamaLokasi);
            AlamatLokasi = itemView.findViewById(R.id.AlamatLokasi);
            NohpLokasi = itemView.findViewById(R.id.NohpLokasi);
            LinkLokasi = itemView.findViewById(R.id.LinkLokasi);
        }
    }
}
