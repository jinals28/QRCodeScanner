package com.example.qrcodescanner.auth;

import static com.example.qrcodescanner.utils.Constants.RC_SIGN_IN;
import static com.example.qrcodescanner.utils.Constants.USER;
import static com.example.qrcodescanner.utils.HelperClass.logErrorMessage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.qrcodescanner.main.MainActivity;
import com.example.qrcodescanner.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initSignInButton();
        initAuthViewModel();
        initGoogleSignInClient();
    }

    private void initGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("816428814399-v6e2og8ilbhvdep6e3mtebpofub4jua0.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void initSignInButton() {
        SignInButton googleSignInButton = findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null ){
                    getGoogleAuthCredentials(googleSignInAccount);
                }
            }catch (ApiException e){
                logErrorMessage(e.getMessage());
            }
        }
    }

    private void getGoogleAuthCredentials(GoogleSignInAccount googleSignInAccount) {
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogleAuthCredential(googleAuthCredential);
    }

    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential) {
        authViewModel.signInWithGoogle(googleAuthCredential);
        authViewModel.authenticatedUserLiveData.observe(this, authenticatedUser -> {
            if (authenticatedUser.isNew){
                createNewUser(authenticatedUser);
            } else {
                goToMainActivity(authenticatedUser);
            }
        });
    }
    private void createNewUser(User authenticatedUser) {
        authViewModel.createUser(authenticatedUser);
        authViewModel.createdUserLiveData.observe(this, user -> {
            if (user.isCreated){
                toast(user.name);
            }
            goToMainActivity(user);
        });
    }

    private void toast(String name) {
        Toast.makeText(this, "Hi "+ name + " Your account was successfully created", Toast.LENGTH_SHORT).show();
    }

    private void goToMainActivity(User user) {
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
        finish();

    }
}