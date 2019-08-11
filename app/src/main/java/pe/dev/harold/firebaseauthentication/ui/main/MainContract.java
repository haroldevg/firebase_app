package pe.dev.harold.firebaseauthentication.ui.main;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import pe.dev.harold.firebaseauthentication.base.BasePresenter;
import pe.dev.harold.firebaseauthentication.base.BaseView;
import pe.dev.harold.firebaseauthentication.model.Movie;
import pe.dev.harold.firebaseauthentication.ui.CommonPresenter;

public class MainContract{

    public interface MainView extends BaseView {
        void onFetchMovies(Query query);
    }

    public interface MainPresenter<V extends MainView> extends BasePresenter<V> {
        void fetchMovies();
        void uploadMovie(Movie movie,byte[] imageAsset);
    }

}
