package pe.dev.harold.firebaseauthentication.ui.login;

import com.google.firebase.auth.AuthCredential;

import pe.dev.harold.firebaseauthentication.base.BasePresenter;
import pe.dev.harold.firebaseauthentication.base.BaseView;

public class LoginContract {

    public interface LoginView extends BaseView {
        void onLoginResult(Boolean isSuccess);
        void onUserLogged(Boolean isSuccess);
    }

    public interface LoginPresenter<V extends LoginView> extends BasePresenter<V> {

        void checkUserLogged();
        void login(String username, String password);
        void googleLogin(AuthCredential credential);

    }

}
