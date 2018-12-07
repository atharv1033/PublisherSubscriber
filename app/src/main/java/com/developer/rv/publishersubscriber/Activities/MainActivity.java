package com.developer.rv.publishersubscriber.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.developer.rv.publishersubscriber.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();

    String name , email;
    Boolean loginStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (user == null) {
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            startActivityForResult(signInIntent, 9999);
        } else {
            name = user.getDisplayName();
            email = user.getEmail();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option_menu_layout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.change_account) {
            AuthUI.getInstance().delete(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 9999) {
            if(resultCode == RESULT_OK) {
                name = firebaseAuth.getCurrentUser().getDisplayName();
                email = firebaseAuth.getCurrentUser().getEmail();

                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                try {
                    db.collection("publisher").document(email).get();
                } catch (Exception ex) {
                    db.collection("publisher").document(email).set(Collections.singletonMap("name",name));
                }

                try {
                    db.collection("subscriber").document(email).get();
                } catch (Exception ex) {
                    db.collection("subscriber").document(email).set(Collections.singletonMap("name",name));
                }

            } else {
                Toast.makeText(this, "Login Failed : Connect To internet", Toast.LENGTH_SHORT).show();
                loginStatus = false;
                }
        }
    }

    void GetPub(View view) {

        if (loginStatus) {
            Intent intent = new Intent(this, PublisherActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Login failed : Reopen app", Toast.LENGTH_SHORT).show();
        }
    }

    void GetSub(View view) {

        if (loginStatus) {
            Intent intent = new Intent(this, SubscriberActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Login failed : Reopen app", Toast.LENGTH_SHORT).show();

        }
    }
}
