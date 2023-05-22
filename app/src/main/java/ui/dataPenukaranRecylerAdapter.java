package ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sampahgo.ProsesPenukaran;
import com.example.sampahgo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import model.dataPenukaran;

public class dataPenukaranRecylerAdapter extends RecyclerView.Adapter<dataPenukaranRecylerAdapter.ViewHolder>{
    Context context;
    private Dialog dialog;
    List<dataPenukaran> listdata;
    CardView cardView;
    public interface Dialog{
        void onClick(int pos);
    }
    public void setDialog(Dialog dialog){
        this.dialog = dialog;
    }
    public dataPenukaranRecylerAdapter(Context context, List<dataPenukaran> listdata) {
        this.context = context;
        this.listdata = listdata;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.datapenukaran_row, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        FirebaseUser user1 = auth.getCurrentUser();
        dataPenukaran user = listdata.get(position);
        String ImageUri;

        final String userId = user1.getUid();

        holder.NamaPenukaran.setText(user.getNamaPenukaran());
        holder.DeskripsiPenukaran.setText(user.getDeskripsiPenukaran());
        holder.PointPenukaran.setText(user.getPointPenukaran());
        ImageUri = user.getImageUri();
        Glide.with(context).load(ImageUri).fitCenter().into(holder.ImageUri);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProsesPenukaran.class);
                intent.putExtra("userID", userId);
                intent.putExtra("penukaranID", user.getPenukaranID());
                intent.putExtra("namaPenukaran", user.getNamaPenukaran());
                intent.putExtra("deskripsiPenukaran", user.getDeskripsiPenukaran());
                intent.putExtra("pointPenukaran", user.getPointPenukaran());
                intent.putExtra("imageUri", user.getImageUri());
                v.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView NamaPenukaran;
        TextView DeskripsiPenukaran;
        TextView PointPenukaran;
        public ImageView ImageUri;
        CardView cardView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            NamaPenukaran = itemView.findViewById(R.id.NamaPenukaran);
            DeskripsiPenukaran = itemView.findViewById(R.id.DeskripsiPenukaran);
            PointPenukaran = itemView.findViewById(R.id.PointPenukaran);
            ImageUri = itemView.findViewById(R.id.ImagePenukaran);
            cardView = itemView.findViewById(R.id.dataBarangPenukaran);
        }
    }
}
