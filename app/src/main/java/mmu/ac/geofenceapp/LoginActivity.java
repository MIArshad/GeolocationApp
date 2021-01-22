package mmu.ac.geofenceapp;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import static android.text.Html.fromHtml;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;

    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    Button buttonLogin;

    DBHelper sqlitHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_login);
        sqlitHelper = new DBHelper(this);
        //if the user hasn't got an account redirect them to another activity
        initCreateAccountTextView();
        //link the views and texfields/inouts
        initViews();

        buttonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    //get the values entered into the edit text fields
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();

                    //authenticate the user
                    User currentUser = sqlitHelper.Authenticate(new User(null, null, email, password));

                    //check to see if the authentication was successful
                    if(currentUser != null)
                    {
                        //show a pop up to let the user know that they have logged in
                        Snackbar.make(buttonLogin, "Successfully Logged In", Snackbar.LENGTH_LONG).show();

                        ///////////////////////////////////////////////////////////////////////////
                        ////////////////insert code to take to home screen activity////////////////
                        ///////////////////////////////////////////////////////////////////////////
                    }
                    //authentication wasn't succesful
                    else
                    {
                        //display a popup message to let the user know
                        Snackbar.make(buttonLogin, "Login Unsuccessful, please try again", Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    public void initCreateAccountTextView()
    {
        //if the user clicks the button to indicate that they don't have an account, start the register activity
        TextView textViewCreateAccount = (TextView) findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setText(fromHtml("<font color='#fffff>I don't have an account yet!</font> <font color='#0c0099'>Create one</font>"));

        textViewCreateAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initViews()
    {
        editTextEmail = findViewById(R.id.login_username);
        editTextPassword = findViewById(R.id.login_pass);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
    }

    //handle fromhtml method deprecation
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    //validate input given by the user
    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        //Handling validation for Email field
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email!");
            Snackbar snackbar = null;
            snackbar.make(buttonLogin, "Please enter valid email!", Snackbar.LENGTH_LONG);
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }

        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Please enter valid password!");
        } else {
            if (Password.length() > 5) {
                valid = true;
                textInputLayoutPassword.setError(null);
            } else {
                valid = false;
                textInputLayoutPassword.setError("Password is to short!");
            }
        }
        return valid;
    }
}
