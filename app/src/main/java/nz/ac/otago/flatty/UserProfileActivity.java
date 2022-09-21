package nz.ac.otago.flatty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import nz.ac.otago.flatty.dao.AccountsDAO;
import nz.ac.otago.flatty.dao.DetailsDAO;
import nz.ac.otago.flatty.domain.Accounts;

public class UserProfileActivity extends Activity {
    Accounts account;
    AccountsDAO dao = new AccountsDAO();
    DetailsDAO detailDAO = new DetailsDAO();

    protected void onCreate(Bundle savedInstanceState) { // what appears when first open the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        account = (Accounts)getIntent().getSerializableExtra("account");
        EditText textFirstName = (EditText) findViewById(R.id.textName);
        EditText textDOB = (EditText) findViewById(R.id.textDOB);
        EditText textDegree = (EditText) findViewById(R.id.textDegree);
        EditText textBudget = (EditText) findViewById(R.id.textBudget);
        EditText textHobbies = (EditText) findViewById(R.id.hobbiesInterest);
        ImageView flattylogo = (ImageView) findViewById(R.id.logoFlatty);
        TextView flattytitle = (TextView) findViewById(R.id.Flatty);
        ImageView profilepic = (ImageView) findViewById(R.id.userImage);
        TextView title = (TextView) findViewById(R.id.pageTitle);
        ImageButton homebtn = (ImageButton) findViewById(R.id.ButtonHome);
        ImageButton matchesbtn = (ImageButton) findViewById(R.id.ButtonMatches);
        ImageButton profilebtn = (ImageButton) findViewById(R.id.ButtonProfile);
        ImageButton settingsbtn = (ImageButton) findViewById(R.id.ButtonSettings);


        textFirstName.setText(account.getFirstName() + " " + account.getLastName());

        textFirstName.setKeyListener(null);
        textDOB.setKeyListener(null);
        textDegree.setKeyListener(null);
        textBudget.setKeyListener(null);
        textHobbies.setKeyListener(null);

        if (account.getDetails().getDateOfBirth() != null) {
            textDOB.setText(account.getDetails().getDateOfBirth().toString());
        }
        if (account.getDetails().getDegree() != null) {
            textDegree.setText(account.getDetails().getDegree());
        }
        if (account.getDetails().getWeeklyBudget() != null) {
            textBudget.setText(account.getDetails().getWeeklyBudget().toString());
        }
        if (account.getDetails().getExtraInfo() != null) {
            textHobbies.setText(account.getDetails().getExtraInfo());
        }


        homebtn.setOnClickListener(
                new ImageButton.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(UserProfileActivity.this, HomePageActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        matchesbtn.setOnClickListener(
                new ImageButton.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(UserProfileActivity.this,MatchesActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        profilebtn.setOnClickListener(
                new ImageButton.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(UserProfileActivity.this,UserProfileActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        settingsbtn.setOnClickListener(
                new ImageButton.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(UserProfileActivity.this,UpdateProfileActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
    }
}