package pe.dev.harold.firebaseauthentication.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pe.dev.harold.firebaseauthentication.R;
import pe.dev.harold.firebaseauthentication.ui.main.MainActivity;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.RegisterView {

    EditText etUsername;
    EditText etPassword;
    Button btnRegister;

    RegisterContract.RegisterPresenter<RegisterContract.RegisterView> _presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _presenter = new RegisterPresenterImpl<RegisterContract.RegisterView>();
        _presenter.attach(this);

        _initialize();
        _setListeners();
    }

    void _initialize(){
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
    }


    void _setListeners(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                _presenter.register(username,password);
            }
        });
    }

    @Override
    public void onRegisterResult(Boolean isSuccess) {
        if(isSuccess){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "An Error Ocurred",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
