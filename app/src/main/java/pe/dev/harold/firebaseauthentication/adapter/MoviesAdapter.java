package pe.dev.harold.firebaseauthentication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.dev.harold.firebaseauthentication.R;
import pe.dev.harold.firebaseauthentication.base.BaseFireStoreAdapter;
import pe.dev.harold.firebaseauthentication.model.Movie;

public class MoviesAdapter extends BaseFireStoreAdapter<MoviesAdapter.MoviesViewHolder> {

    private MoviesAdapterListener listener;

    private boolean showDetailSection = true;
    private boolean showRatingBar = true;

    public MoviesAdapter(Query query) {
        super(query);
    }

    public void setListener(MoviesAdapterListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, int position) {
        final DocumentSnapshot documentSnapshot = mDocumentSnapshots.get(position);
        final Movie movie = documentSnapshot.toObject(Movie.class);
        if (movie != null) {
            if(movie.getPoster() != "")
                Picasso.get().load(movie.getPoster()).fit()
                        .into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onMovieSelected(documentSnapshot);
                    }
                }
            });
            holder.ratingBar.setRating(movie.getAverageRatings());
            holder.tvTitle.setText(movie.getTitle());
            holder.tvDate.setText(movie.getReleaseDate());
            holder.tvRating.setText(String.format("Ratings %s", "" + movie.getAverageRatings()));
            holder.movieContainer.setVisibility(showDetailSection ? View.VISIBLE : View.GONE);
            holder.ratingContainer.setVisibility(showDetailSection && showRatingBar ? View.VISIBLE : View.GONE);
            holder.tvRating.setVisibility(showRatingBar ? View.VISIBLE : View.GONE);

        }

    }

    public interface MoviesAdapterListener {
        void onMovieSelected(DocumentSnapshot documentSnapshot);
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_movie)
        ImageView imageView;

        @BindView(R.id.rb_movie)
        RatingBar ratingBar;

        @BindView(R.id.fm_movie_container)
        View movieContainer;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_date)
        TextView tvDate;

        @BindView(R.id.tv_rating)
        TextView tvRating;

        @BindView(R.id.ll_rating)
        View ratingContainer;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
