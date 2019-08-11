package pe.dev.harold.firebaseauthentication.base;

public interface BasePresenter<V extends BaseView> {

    void attach(V view);

    void detach();
}