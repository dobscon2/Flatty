package nz.ac.otago.flatty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import nz.ac.otago.flatty.dao.AccountsDAO;
import nz.ac.otago.flatty.dao.DetailsDAO;
import nz.ac.otago.flatty.domain.Accounts;
import nz.ac.otago.flatty.domain.Details;

public class MainActivity extends AppCompatActivity {

    private String email;
    private String password;
    private Button loginButton;
    private Button createButton;
    private AccountsDAO dao = new AccountsDAO();
    private DetailsDAO detailsDAO = new DetailsDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logindesign);
        OnClickButtonListener();
    }

    private void OnClickButtonListener() {
        loginButton = (Button) findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener((View.OnClickListener) v -> {
            EditText emailText = (EditText) findViewById(R.id.TextEmail);
            EditText passwordText = (EditText) findViewById(R.id.TextPassword);

            email = emailText.getText().toString();
            password = passwordText.getText().toString();
            if (dao.validateLogin(email, password)) {
                Accounts account = dao.getAccount(email);
                Details details = detailsDAO.getDetails(account);
                account.setDetails(details);
                System.out.println(account.toString());
                System.out.println(details.toString());
                Intent next = new Intent(MainActivity.this, HomePageActivity.class);
                next.putExtra("account", account);
                startActivity(next);
            } else {
                AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Error")
                        .setMessage("Incorrect Email or Password")
                        .setPositiveButton("Ok", (dialog, which) -> finish()).show();
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> alert.dismiss());
            }
        });
        createButton = (Button) findViewById(R.id.buttonCreateAccount);
        createButton.setOnClickListener(v -> {
            Intent next = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(next);
        });
    }


}