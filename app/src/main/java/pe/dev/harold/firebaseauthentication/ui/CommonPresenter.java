package pe.dev.harold.firebaseauthentication.ui;

import pe.dev.harold.firebaseauthentication.base.BasePresenter;
import pe.dev.harold.firebaseauthentication.base.BaseView;

public class CommonPresenter<V extends BaseView> implements BasePresenter<V> {

    private V view;

    @Override
    public void attach(V view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    public V getView(){
        return view;
    }
}
