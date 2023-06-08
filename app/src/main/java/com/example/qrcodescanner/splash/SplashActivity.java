package com.example.qrcodescanner.splash;

import static com.example.qrcodescanner.utils.Constants.USER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.qrcodescanner.main.MainActivity;
import com.example.qrcodescanner.auth.AuthActivity;
import com.example.qrcodescanner.auth.User;

public class SplashActivity extends AppCompatActivity {
    SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSplashViewModel();
        checkIfUserIsAuthenticated();
    }

    private void checkIfUserIsAuthenticated() {
        splashViewModel.checkIfUserIsAuthenticated();
        splashViewModel.isUserAuthenticatedLiveData.observe(this, user -> {
            if (!user.isAuthenticated) {
                goToAuthInActivity();
                finish();
            } else {
                getUserFromDatabase(user.uid);
            }
        });
    }

    private void getUserFromDatabase(String uid) {
        splashViewModel.setUid(uid);
        splashViewModel.userLiveData.observe(this , user -> {
            goToMainActivity(user);
            finish();
        });
    }

    private void goToMainActivity(User user) {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
    }

    private void goToAuthInActivity() {
        Intent intent = new Intent(SplashActivity.this , AuthActivity.class);
        startActivity(intent);
    }

    private void initSplashViewModel() {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }
}