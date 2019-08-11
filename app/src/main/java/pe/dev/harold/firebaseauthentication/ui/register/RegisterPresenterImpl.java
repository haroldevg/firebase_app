package pe.dev.harold.firebaseauthentication.ui.register;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pe.dev.harold.firebaseauthentication.ui.CommonPresenter;
import pe.dev.harold.firebaseauthentication.ui.main.MainActivity;

public class RegisterPresenterImpl<V extends RegisterContract.RegisterView> extends CommonPresenter<V> implements RegisterContract.RegisterPresenter<V> {

    private FirebaseAuth firebaseAuth;

    RegisterPresenterImpl(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void register(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    getView().onRegisterResult(true);
                }
                else{
                    getView().onRegisterResult(false);
                }
            }
        });
    }
}
