package pe.dev.harold.firebaseauthentication.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.dev.harold.firebaseauthentication.R;
import pe.dev.harold.firebaseauthentication.adapter.MoviesAdapter;
import pe.dev.harold.firebaseauthentication.model.Movie;
import pe.dev.harold.firebaseauthentication.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {

    MainContract.MainPresenter<MainContract.MainView> _presenter;
    private MoviesAdapter adapter;


    @BindView(R.id.rv_movies)
    RecyclerView rvMovies;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.floating_action_button)
    FloatingActionButton fabMovies;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    private FirebaseFirestore firebaseFirestore;
    private StorageReference mStorageRef;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(120)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config);
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    toolbar.setTitle(mFirebaseRemoteConfig.getString("title"));
                }
            }
        });


        FirebaseFirestore.setLoggingEnabled(true);

        ButterKnife.bind(this);
        toolbar.setTitle(mFirebaseRemoteConfig.getString("title"));
        setSupportActionBar(toolbar);
        _presenter = new MainPresenterImpl<MainContract.MainView>(getApplicationContext(),firebaseFirestore,mStorageRef);
        _presenter.attach(this);
        _setOnClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void _setOnClickListener(){
        fabMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie aux = new Movie();
                aux.setTitle("Anabelle");
                aux.setTotalReviews(0);
                aux.setReleaseDate("2017-07-28");
                aux.setOverview("Several years after the tragic death of their little girl, a dollmaker and his wife welcome a nun and several girls from a shuttered orphanage into their home, soon becoming the target of the dollmaker's possessed creation, Annabelle.");
                aux.setAverageRatings(3.5f);
                aux.setPoster("");

                AssetManager assetManager = MainActivity.this.getAssets();
                InputStream istr;
                Bitmap bitmap;

                try {
                    istr = assetManager.open("movie.jpg");
                    bitmap = BitmapFactory.decodeStream(istr);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] data = outputStream.toByteArray();

                    _presenter.uploadMovie(aux,data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        _presenter.fetchMovies();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListeningForLiveEvents();
        _presenter.detach();
    }

    @Override
    public void onFetchMovies(Query query) {
        adapter = new MoviesAdapter(query) {
            @Override
            public void onEventTriggered() {
                super.onEventTriggered();
                if (getItemCount() == 0) {
                    rvMovies.setVisibility(View.GONE);
                } else {
                    rvMovies.setVisibility(View.VISIBLE);
                }
            }
        };

        adapter.startListeningForLiveEvents();

        adapter.setListener(new MoviesAdapter.MoviesAdapterListener() {
            @Override
            public void onMovieSelected(DocumentSnapshot documentSnapshot) {
                //startActivity(MovieDetailActivity.getIntent(MoviesListActivity.this, documentSnapshot.getId()));
            }
        });

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mLinearLayoutManager = new LinearLayoutManager(this);

        rvMovies.setLayoutManager(mGridLayoutManager);

        rvMovies.setAdapter(adapter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
