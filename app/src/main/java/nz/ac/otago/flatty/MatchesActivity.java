package nz.ac.otago.flatty;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import nz.ac.otago.flatty.dao.AccountsDAO;
import nz.ac.otago.flatty.dao.DetailsDAO;
import nz.ac.otago.flatty.domain.Accounts;

public class MatchesActivity extends Activity {
    Accounts account;
    AccountsDAO dao = new AccountsDAO();
    LinearLayout matchesList;
    protected void onCreate(Bundle savedInstanceState) { // what appears when first open the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_matches);
        account = (Accounts)getIntent().getSerializableExtra("account");
        HashSet<Integer> matches = dao.getMatches(account);

        ArrayList<Accounts> matchedAccounts = new ArrayList<>();
        for (int matchID : matches) {
            matchedAccounts.add(dao.getAccount(matchID));
        }

        matchesList = findViewById(R.id.matches_list);
        ImageButton homebtn = (ImageButton) findViewById(R.id.ButtonHome);
        ImageButton matchesbtn = (ImageButton) findViewById(R.id.ButtonMatches);
        ImageButton profilebtn = (ImageButton) findViewById(R.id.ButtonProfile);
        ImageButton settingsbtn = (ImageButton) findViewById(R.id.ButtonSettings);

        for (Accounts account: matchedAccounts) {
            TextView textView = new TextView(MatchesActivity.this);
            textView.setText(account.getFirstName() + " " + account.getLastName());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 0, 10);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setTextSize(30);
            matchesList.addView(textView);
        }

        homebtn.setOnClickListener(
                new ImageButton.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(MatchesActivity.this, HomePageActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        matchesbtn.setOnClickListener(
                new ImageButton.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(MatchesActivity.this,MatchesActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        profilebtn.setOnClickListener(
                new ImageButton.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(MatchesActivity.this,UserProfileActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        settingsbtn.setOnClickListener(
                new ImageButton.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(MatchesActivity.this,UpdateProfileActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
    }
}
