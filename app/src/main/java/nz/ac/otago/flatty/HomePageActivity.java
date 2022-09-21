package nz.ac.otago.flatty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import nz.ac.otago.flatty.dao.AccountsDAO;
import nz.ac.otago.flatty.dao.DetailsDAO;
import nz.ac.otago.flatty.domain.Accounts;

public class HomePageActivity extends Activity implements GestureDetector.OnGestureListener {
    Accounts account;
    AccountsDAO dao = new AccountsDAO();
    DetailsDAO detailsDAO = new DetailsDAO();
    ArrayList<Accounts> potentialMatches;
    int matchIndex = 0;

    TextView mtcName;
    TextView mtcAge;
    TextView mtcDietary;
    TextView mtcDegree;
    TextView mtcBudget;
    TextView mtcBio;

    private GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;

    protected void onCreate(Bundle savedInstanceState) { // what appears when first open the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.gestureDetector = new GestureDetector(HomePageActivity.this, this);
        account = (Accounts) getIntent().getSerializableExtra("account");
        potentialMatches = detailsDAO.match(account);
        account.setMatches(dao.getMatches(account));
        System.out.println(Arrays.toString(potentialMatches.toArray())); // testing to see if ArrayList is generated onCreate
        System.out.println(account.toString());
        System.out.println(account.getDetails().toString());
        FloatingActionButton button1 = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        FloatingActionButton button2 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        mtcName = (TextView) findViewById(R.id.textDisplayName);
        mtcAge = (TextView) findViewById(R.id.textDisplayAge);
        mtcDietary = (TextView) findViewById(R.id.textDisplayDietary);
        mtcDegree = (TextView) findViewById(R.id.textDisplayDegree);
        mtcBudget = (TextView) findViewById(R.id.textDisplayBudget);
        mtcBio = (TextView) findViewById(R.id.userBio);

        ImageButton homebtn = (ImageButton) findViewById(R.id.ButtonHome);
        ImageButton matchesbtn = (ImageButton) findViewById(R.id.ButtonMatches);
        ImageButton profilebtn = (ImageButton) findViewById(R.id.ButtonProfile);
        ImageButton settingsbtn = (ImageButton) findViewById(R.id.ButtonSettings);
        ImageView userImage = (ImageView) findViewById(R.id.userImage);

        nextPotentialMatch();


        homebtn.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(HomePageActivity.this, HomePageActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        matchesbtn.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(HomePageActivity.this, MatchesActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        profilebtn.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(HomePageActivity.this, UserProfileActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );

        button2.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("matchedIndex: " + matchIndex);
                        System.out.println("array size: " + potentialMatches.size());
                        if (matchIndex < potentialMatches.size()) {
                            Accounts matchedAccount = potentialMatches.get(matchIndex);
                            HashSet<Integer> matches = account.getMatches();
                            matches.add(matchedAccount.getAccountID());
                            account.setMatches(matches);
                            dao.addMatch(account);
                            matchIndex++;
                            nextPotentialMatch();
                        } else {
                            // tell user no more matches available
                            System.out.println(Arrays.toString(account.getMatches().toArray()));
                            Toast.makeText(HomePageActivity.this, "No more matches available", Toast.LENGTH_SHORT).show();
                        }

                        // carry first name to my matches page
                    }
                }
        );

        button1.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        if (matchIndex < potentialMatches.size()) {
                            matchIndex++;
                            nextPotentialMatch();
                        } else {
                            Toast.makeText(HomePageActivity.this, "No more matches available", Toast.LENGTH_SHORT).show();
                        }

                        // display next potential match - do not add to matches page
                    }
                }
        );


        settingsbtn.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(HomePageActivity.this, UpdateProfileActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
    }

    public void nextPotentialMatch() {
        System.out.println(matchIndex);
        System.out.println("Array size: " + potentialMatches.size());
        if (matchIndex < potentialMatches.size()) {
            if (!potentialMatches.get(matchIndex).getAccountID().equals(account.getAccountID())) {
                System.out.println("true account");
                Date todaysDate = new Date();

                Accounts potentialAccount = potentialMatches.get(matchIndex);
                mtcName.setText(potentialAccount.getFirstName() + " " + potentialAccount.getLastName());
                Date potentialAccountDOB = potentialAccount.getDetails().getDateOfBirth();

                int birthyear = potentialAccountDOB.getYear() + 1900;
                System.out.println("Birth Year: " + birthyear);
                int currentyear = todaysDate.getYear() + 1900;
                System.out.println("Current Year: " + currentyear);
                int age = currentyear - birthyear;
                int birthmonth = potentialAccountDOB.getMonth() + 1;
                int currentmonth = todaysDate.getMonth() + 1;
                System.out.println("Birth Month: " + birthmonth);
                System.out.println("Current Month: " + currentmonth);
                if (currentmonth < birthmonth) {
                    age--;
                } else if (birthmonth == currentmonth) {
                    int birthday = potentialAccountDOB.getDate();
                    int currentday = todaysDate.getDate();
                    if (currentday > birthday) {
                        age--;
                    }
                }
                mtcAge.setText(Integer.toString(age));

                mtcDietary.setText(potentialAccount.getDetails().getDietRequirements());
                mtcDegree.setText(potentialAccount.getDetails().getDegree());
                mtcBudget.setText(potentialAccount.getDetails().getWeeklyBudget().toString());
                mtcBio.setText(potentialAccount.getDetails().getExtraInfo());
            } else {
                matchIndex++;
                nextPotentialMatch();
            }
        } else {
            // do nothing - show error
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;

            case MotionEvent.ACTION_UP:
                x2 = event.getX();

                float valueX = x2 - x1;

                if (Math.abs(valueX) > MIN_DISTANCE) {
                    if (x2 > x1) {
                        // swipe right

//                        Accounts matchedAccount = potentialMatches.get(matchIndex);
//                        ArrayList<Integer> matches = account.getMatches();
//                        matches.add(matchedAccount.getAccountID());
//                        account.setMatches(matches);
//                        matchIndex++;
//                        nextPotentialMatch();


                    } else {
                        // swipe left
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}