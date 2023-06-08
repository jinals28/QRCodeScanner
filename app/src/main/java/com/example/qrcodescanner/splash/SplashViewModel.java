package com.example.qrcodescanner.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.qrcodescanner.auth.User;

public class SplashViewModel extends AndroidViewModel {

    private  SplashRepository splashRepository;

    LiveData<User> isUserAuthenticatedLiveData;
    LiveData<User> userLiveData;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        splashRepository = new SplashRepository();
    }

    void checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticatedInFirebase();
    }

    void setUid(String uid){
        userLiveData = splashRepository.addUserToLiveData(uid);
    }
}
