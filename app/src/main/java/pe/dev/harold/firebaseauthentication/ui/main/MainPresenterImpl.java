package pe.dev.harold.firebaseauthentication.ui.main;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.List;

import pe.dev.harold.firebaseauthentication.model.Movie;
import pe.dev.harold.firebaseauthentication.ui.CommonPresenter;
import pe.dev.harold.firebaseauthentication.utils.ImageUtils;
import pe.dev.harold.firebaseauthentication.utils.StoreConstants;

public class MainPresenterImpl <V extends MainContract.MainView> extends CommonPresenter<V> implements MainContract.MainPresenter<V> {

    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference mStorageRef;

    MainPresenterImpl(Context context,FirebaseFirestore firebaseFirestore,StorageReference storageReference){
        this.context = context;
        this.firebaseFirestore = firebaseFirestore;
        this.mStorageRef = storageReference;
    }

    @Override
    public void fetchMovies() {
        Query query = firebaseFirestore.collection(StoreConstants.COLLECTION_MOVIES);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot snapshots = task.getResult();
                    List<DocumentSnapshot> a =  snapshots.getDocuments();
                    Log.d("SNAPSHOT TEST","HOLA");
                }
            }
        });
        getView().onFetchMovies(query);
    }

    @Override
    public void uploadMovie(Movie movie,byte[] imageAsset) {

        firebaseFirestore.collection(StoreConstants.COLLECTION_MOVIES).add(movie)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        StorageReference imgRef = mStorageRef.child("images/"+documentReference.getId()+".jpg");
                        imgRef.putBytes(imageAsset).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        firebaseFirestore.collection(StoreConstants.COLLECTION_MOVIES).document(documentReference.getId()).update("poster",uri.toString());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                        exception.printStackTrace();

                                    }
                                });
                            }
                        });
                    }
                });

    }
}
