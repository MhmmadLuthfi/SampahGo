package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sampahgo.R;

import java.util.List;

import model.jemput;

public class PenjemputanRecylerAdapter extends RecyclerView.Adapter<PenjemputanRecylerAdapter.ViewHolder> {
    private Context context;
    private List<jemput> PenjemputanList;

    public PenjemputanRecylerAdapter(Context context, List<jemput> harikuList){
        this.context = context;
        this.PenjemputanList = harikuList;
    }
    @NonNull
    @Override
    public PenjemputanRecylerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.penjemputan_row, viewGroup, false);
        return new ViewHolder(view, context);
    }
    @Override
    public void onBindViewHolder(@NonNull PenjemputanRecylerAdapter.ViewHolder holder, int position){
        jemput jemput = PenjemputanList.get(position);
        String imageUrl;
        holder.Kategori.setText(jemput.getKategori());
        holder.Alamat.setText(jemput.getAlamat());
        holder.Tanggal.setText(jemput.getTanggal());
        holder.Waktu.setText(jemput.getWaktu());
        imageUrl = jemput.getImageUri();

        String waktuUp = (String) DateUtils.getRelativeTimeSpanString(jemput.getTimeAdded().getSeconds()*1000);
        holder.tanggalAdd.setText(waktuUp);

        /** *Menggunakan Glide Library untuk menampilkan gambar postingan */

        Glide.with(context).load(imageUrl).fitCenter().into(holder.image);

    }
    @Override
    public int getItemCount(){
        return PenjemputanList.size();
    }

    //View Holder
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView Kategori, Alamat, Tanggal, Waktu, tanggalAdd;
        public ImageView image;

        public ViewHolder(@NonNull View itemView, Context cxt){
            super(itemView);
            context=cxt;

            //Widget ini dari harikurow.xml
            Kategori = itemView.findViewById(R.id.KategoriPenjemputan);
            Alamat = itemView.findViewById(R.id.PenjemputanListAlamat);
            Tanggal = itemView.findViewById(R.id.PenjemputanListTanggal);
            Waktu = itemView.findViewById(R.id.PenjemputanListWaktu);
            tanggalAdd = itemView.findViewById(R.id.PenjemputanTimeStampList);
            image = itemView.findViewById(R.id.PenjemputanImageList);
        }
    }
}
