package com.example.qrcodescanner.auth;

import static com.example.qrcodescanner.utils.Constants.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthCredential;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepository authRepository;
    LiveData<User> authenticatedUserLiveData;
    LiveData<User> createdUserLiveData;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }

    void createUser(User authenticatedUser){
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(authenticatedUser);
    }
}
