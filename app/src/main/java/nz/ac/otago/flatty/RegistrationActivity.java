package nz.ac.otago.flatty;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import nz.ac.otago.flatty.dao.DetailsDAO;
import nz.ac.otago.flatty.domain.Accounts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.TextureView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.CheckBox;

import nz.ac.otago.flatty.domain.Accounts;
import nz.ac.otago.flatty.domain.Details;
import nz.ac.otago.flatty.dao.AccountsDAO;

import android.content.Intent;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import android.gesture.GestureLibraries;
import android.view.View.OnClickListener;
import android.view.View;

public class RegistrationActivity extends Activity {
    Accounts acc1 = new Accounts();
    AccountsDAO dao = new AccountsDAO();
    DetailsDAO detailsDAO = new DetailsDAO();

    protected void onCreate(Bundle savedInstanceState) { // what appears when first open the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        //ImageView imageView2 = (ImageView) findViewById(R.id.imageView2); //flatty house icon
        TextView textView2 = (TextView) findViewById(R.id.textView2); // Flatty title
        TextView textSignupMessage = (TextView) findViewById(R.id.textSignupMessage); // sign up message
        EditText TextFirstName = (EditText) findViewById(R.id.TextFirstName); // first name text line
        EditText TextLastName = (EditText) findViewById(R.id.TextLastName); // last name text line
        EditText TextEmail = (EditText) findViewById(R.id.TextEmail); // email text line
        EditText TextPassword = (EditText) findViewById(R.id.TextPassword); // enter password
        CheckBox CheckBoxTermsConditions = (CheckBox) findViewById(R.id.CheckBoxTermsConditions); // terms and conditions

        Button ButtonCancel = (Button) findViewById(R.id.ButtonCancel); // cancel button
        Button ButtonRegister = (Button) findViewById(R.id.ButtonRegister); // register button

//        ButtonRegister.setOnClickListener(
//                new Button.OnClickListener() {
//                    public void onClick(View v) {
//                        String firstname = String.valueOf(TextFirstName.getText());
//                        String lastname = String.valueOf(TextLastName.getText());
//                        String email = String.valueOf(TextEmail.getText());
//                        String password = String.valueOf(TextPassword.getText());
//
//                        acc1.setFirstName(firstname);
//                        acc1.setLastName(lastname);
//                        acc1.setEmail(email);
//                        acc1.setPassword(password);
//
//                        System.out.println(acc1.toString());
//                        dao.createAccount(acc1); // create account for this user.
//
//
//                    }
//
//                }
//        );
        //Intent intent1 = new Intent(RegistrationActivity.this, HomePageActivity.class);
        // startActivity(intent1);

        ButtonCancel.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextFirstName.getText().clear();
                        TextLastName.getText().clear();
                        TextEmail.getText().clear();
                        TextPassword.getText().clear();
                        Intent next = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(next);
                    }
                }
        );


//    public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
//        private String[] degree = {"Science", "Arts", "English", "IT", "Commerce", "Health Science", "Engineering",
//                "Politics", "Sport", "Teaching", "Law", "Other"};
//
//        private String[] budget = {"Under $100", "$100-150", "$150-200", "$200-250", "$250+"};
//
//        private String[] ageBracket = {"18-20", "20-23", "24-27", "28-30", "Other"};
//    }
        ButtonRegister.setOnClickListener(v -> {
            String firstname = String.valueOf(TextFirstName.getText());
            String lastname = String.valueOf(TextLastName.getText());
            String email = String.valueOf(TextEmail.getText());
            String password = String.valueOf(TextPassword.getText());
            if (CheckBoxTermsConditions.isChecked() == true) {
                if (firstname.length() > 1 && lastname.length() > 1 && email.length() > 1 && password.length() > 1) {
                    if (dao.getAccount(email) == null) {
                        dao.createAccount(new Accounts(firstname, lastname, email, password)); // create account for this user.
                        Accounts account = dao.getAccount(email);
                        Date todayDate = new Date();
                        System.out.println(todayDate.toString());
                        account.setDetails(new Details(account.getAccountID(), new Date(), false, "unknown", "unknown", "Any"));
                        detailsDAO.createDetails(account);
                        Intent next = new Intent(RegistrationActivity.this, UpdateProfileActivity.class);
                        next.putExtra("account", account);
                        startActivity(next);
                    } else {
                        AlertDialog alert = new AlertDialog.Builder(RegistrationActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Error")
                                .setMessage("This email has already been used")
                                .setPositiveButton("Ok", (dialog, which) -> finish()).show();
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> alert.dismiss());
                    }
                } else {
                    AlertDialog alert = new AlertDialog.Builder(RegistrationActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Error")
                            .setMessage("Please fill in all text fields")
                            .setPositiveButton("Ok", (dialog, which) -> finish()).show();
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> alert.dismiss());
                }
            } else {
                AlertDialog alert = new AlertDialog.Builder(RegistrationActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Error")
                        .setMessage("Please agree to the terms and conditions in order to use this app")
                        .setPositiveButton("Ok", (dialog, which) -> finish()).show();
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> alert.dismiss());
            }
        });
    }
}