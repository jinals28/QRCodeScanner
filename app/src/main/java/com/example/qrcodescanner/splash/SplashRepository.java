package com.example.qrcodescanner.splash;

import androidx.lifecycle.MutableLiveData;

import com.example.qrcodescanner.auth.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.qrcodescanner.utils.Constants.RC_SIGN_IN;
import static com.example.qrcodescanner.utils.Constants.USER;
import static com.example.qrcodescanner.utils.Constants.USERS;
import static com.example.qrcodescanner.utils.HelperClass.logErrorMessage;


@SuppressWarnings("ConstantConditions")
class SplashRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private User user = new User();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = rootRef.collection(USERS);

    MutableLiveData<User> checkIfUserIsAuthenticatedInFirebase() {
        MutableLiveData<User> isUserAuthenticateInFirebaseMutableLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            user.isAuthenticated = false;
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user);
        } else {
            user.uid = firebaseUser.getUid();
            user.isAuthenticated = true;
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user);
        }
        return isUserAuthenticateInFirebaseMutableLiveData;
    }

    MutableLiveData<User> addUserToLiveData(String uid) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        usersRef.document(uid).get().addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful()) {
                DocumentSnapshot document = userTask.getResult();
                if(document.exists()) {
                    User user = document.toObject(User.class);
                    userMutableLiveData.setValue(user);
                }
            } else {
                logErrorMessage(userTask.getException().getMessage());
            }
        });
        return userMutableLiveData;
    }
}
