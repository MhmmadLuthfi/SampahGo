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

import model.tukar;

public class PenukaranRecylerAdapter extends RecyclerView.Adapter<PenukaranRecylerAdapter.ViewHolder>{
    private Context context;
    private List<tukar> PenukaranList;

    public PenukaranRecylerAdapter(Context context, List<tukar> harikuList){
        this.context = context;
        this.PenukaranList = harikuList;
    }
    @NonNull
    @Override
    public PenukaranRecylerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.penukaran_row, viewGroup, false);
        return new ViewHolder(view, context);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        tukar tukar = PenukaranList.get(position);
        String imageUrl;
        holder.namaPenukaran.setText(tukar.getnamaPenukaran());
        holder.deskripsiPenukaran.setText(tukar.getDeskripsiPenukaran());
        holder.pointPenukaran.setText(tukar.getPointPenukaran());
        holder.alamatPenukaran.setText(tukar.getAlamatPenukaran());
        holder.nohpPenukaran.setText(tukar.getNohpPenukaran());
        imageUrl = tukar.getImageUri();

        String waktuUp = (String) DateUtils.getRelativeTimeSpanString(tukar.getTimeAdded().getSeconds()*1000);
        holder.tanggalAdd.setText(waktuUp);

        /** *Menggunakan Glide Library untuk menampilkan gambar postingan */

        Glide.with(context).load(imageUrl).fitCenter().into(holder.imageUri);
    }
    @Override
    public int getItemCount(){
        return PenukaranList.size();
    }

    //View Holder
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView namaPenukaran, deskripsiPenukaran, pointPenukaran, alamatPenukaran, nohpPenukaran, tanggalAdd;
        public ImageView imageUri;

        public ViewHolder(@NonNull View itemView, Context cxt){
            super(itemView);
            context=cxt;

            //Widget ini dari harikurow.xml
            namaPenukaran = itemView.findViewById(R.id.namaPenukaranPengguna);
            deskripsiPenukaran = itemView.findViewById(R.id.deskripsiPenukaranPengguna);
            pointPenukaran = itemView.findViewById(R.id.pointPenukaranPengguna);
            alamatPenukaran = itemView.findViewById(R.id.alamatPenukaranPengguna);
            nohpPenukaran = itemView.findViewById(R.id.nohpPenukaranPengguna);
            tanggalAdd = itemView.findViewById(R.id.PenukaranTimeStampList);
            imageUri = itemView.findViewById(R.id.imagePenukaranPengguna);
        }
    }
}
