package pe.dev.harold.firebaseauthentication.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import pe.dev.harold.firebaseauthentication.ui.main.MainActivity;
import pe.dev.harold.firebaseauthentication.R;
import pe.dev.harold.firebaseauthentication.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LoginContract.LoginView {

    EditText _etUsername;
    EditText _etPassword;

    Button _loginButton;
    Button _registerButton;
    SignInButton _googleButton;


    private ProgressBar _pbDialog;

    /*
    FirebaseAuth _firebaseAuth;*/

    private GoogleSignInClient mGoogleSignInClient;

    LoginContract.LoginPresenter<LoginContract.LoginView> _presenter;

    private static final int RC_SIGN_IN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _presenter = new LoginPresenterImpl<LoginContract.LoginView>();
        _presenter.attach(this);
        _presenter.checkUserLogged();

        _initialize();
        _setListeners();
        _googleConfiguration();
    }


    private void _googleConfiguration(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                        .requestIdToken(getString(R.string.default_web_client_id))
                                                        .requestEmail()
                                                        .build();



        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void _initialize(){
        _loginButton = (Button) findViewById(R.id.login);
        _registerButton = (Button) findViewById(R.id.btn_register);
        _googleButton = (SignInButton) findViewById(R.id.google_button);
        _etUsername = (EditText) findViewById(R.id.et_username);
        _etPassword = (EditText) findViewById(R.id.et_password);
        _pbDialog = (ProgressBar) findViewById(R.id.pb_dialog);
    }

    private void _setListeners(){
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = _etUsername.getText().toString();
                String password = _etPassword.getText().toString();
                _presenter.login(username,password);
            }
        });
        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        _googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _singIn();
            }
        });
    }

    private void _singIn(){
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    private void _authWithGoogle(final GoogleSignInAccount account){
        final AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        _presenter.googleLogin(authCredential);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {
                    _authWithGoogle(account);
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.getErrorMessage() != null) {
            Log.d("GOOGLE_SIGNIN_ERROR",connectionResult.getErrorMessage());
        }
    }

    @Override
    public void onLoginResult(Boolean isSuccess) {
        if(isSuccess){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(LoginActivity.this, "An error ocurred", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUserLogged(Boolean isSuccess) {
        if(isSuccess){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void showLoading() {
        _pbDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        _pbDialog.setVisibility(View.GONE);
    }
}
