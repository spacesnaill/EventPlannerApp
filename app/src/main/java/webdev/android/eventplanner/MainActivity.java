package webdev.android.eventplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    boolean signUpCheck = true;
    TextView LoginTextView;
    TextView title;
    EditText email;
    EditText userName;
    EditText password;
    RelativeLayout background;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginTextView = (TextView) findViewById(R.id.LoginTextView);
        title = (TextView) findViewById(R.id.title);
        email = (EditText) findViewById(R.id.email);
        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        background = (RelativeLayout) findViewById(R.id.background);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        password.setOnKeyListener(this);

        LoginTextView.setOnClickListener(this);
        background.setOnClickListener(this);
        toolbar.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            showEventListActivity();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

    //code for user sign up and sign in
    public void signUp(View view){



        if(signUpCheck) {


            if (TextUtils.isEmpty(userName.getText().toString()) || TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(email.getText().toString())) {
                Toast.makeText(this, "A Username, a Password, and an Email are required.", Toast.LENGTH_SHORT).show();
            } else {

                ParseUser user = new ParseUser();

                user.setUsername(userName.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            //Log.i("Signup", "Successful");
                            showEventListActivity();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else{


            ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if(user != null){
                        //Log.i("Signup", "Login Successful");
                        showEventListActivity();
                    }
                    else{
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }



    } //end of signUp method

    //code for the Already have an Account? interactive text view
    public void onClick(View view){

        if(view.getId() == R.id.LoginTextView) {

            Button signUpButton = (Button) findViewById(R.id.signUp);

            if(signUpCheck) {

                signUpCheck = false;
                signUpButton.setText("Log In");
                LoginTextView.setText("Don't have an account?");
                title.setText("Log In");
                email.setVisibility(view.GONE);
            }
            else{

                signUpCheck = true;
                signUpButton.setText("Sign Up");
                LoginTextView.setText("Already have an account?");
                title.setText("Sign Up");
                email.setVisibility(view.VISIBLE);

            }
        }
        else if(view.getId() == R.id.background || view.getId() == R.id.toolbar) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }

    }

    //checking if the Enter key is pressed when in the Password field
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent){
        if( i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            signUp(view);
            return true;
        }
        return false;
    }

    public void showEventListActivity() {

        Intent intent = new Intent(getApplicationContext(), EventList.class);
        intent.putExtra("flag", "login");
        startActivity(intent);

    }

}
