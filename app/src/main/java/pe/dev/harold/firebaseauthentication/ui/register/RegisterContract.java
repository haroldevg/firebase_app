package pe.dev.harold.firebaseauthentication.ui.register;

import com.google.firebase.auth.AuthCredential;

import pe.dev.harold.firebaseauthentication.base.BasePresenter;
import pe.dev.harold.firebaseauthentication.base.BaseView;

public class RegisterContract {

    public interface RegisterView extends BaseView {
        void onRegisterResult(Boolean isSuccess);
    }

    public interface RegisterPresenter<V extends RegisterView> extends BasePresenter<V> {

        void register(String username, String password);

    }

}
