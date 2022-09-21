package nz.ac.otago.flatty;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import nz.ac.otago.flatty.dao.AccountsDAO;
import nz.ac.otago.flatty.dao.DetailsDAO;
import nz.ac.otago.flatty.domain.Accounts;
import nz.ac.otago.flatty.domain.Details;

public class UpdateProfileActivity extends Activity {
    Accounts account;
    AccountsDAO dao = new AccountsDAO();
    DetailsDAO detailDAO = new DetailsDAO();
    private Button datePicker;

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) { // what appears when first open the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        account = (Accounts)getIntent().getSerializableExtra("account");
        datePicker = (Button) findViewById(R.id.datePicker);
        Spinner degreeSpinner = (Spinner) findViewById(R.id.degreeDropdown);
        ArrayAdapter<CharSequence> degreeAdapter = ArrayAdapter.createFromResource(this,
                R.array.degrees, android.R.layout.simple_spinner_item);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degreeSpinner.setAdapter(degreeAdapter);
        Spinner dietSpinner = (Spinner) findViewById(R.id.dietaryDropdown);
        ArrayAdapter<CharSequence> dietAdapter = ArrayAdapter.createFromResource(this,
                R.array.diets, android.R.layout.simple_spinner_item);
        dietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietSpinner.setAdapter(dietAdapter);

        Spinner budgetSpinner = (Spinner) findViewById(R.id.budgetDropdown);
        ArrayAdapter<CharSequence> budgetAdapter = ArrayAdapter.createFromResource(this,
                R.array.budgets, android.R.layout.simple_spinner_item);
        budgetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetSpinner.setAdapter(budgetAdapter);

        EditText textFirstName = (EditText) findViewById(R.id.textName); //name field
        EditText textDOB = (EditText) findViewById(R.id.textDOB);
        Spinner textDegree = (Spinner) findViewById(R.id.degreeDropdown);
        Spinner textBudget = (Spinner) findViewById(R.id.budgetDropdown);
        EditText textHobbies = (EditText) findViewById(R.id.textAdditional);
        Spinner dietRequirement = (Spinner) findViewById(R.id.dietaryDropdown);
        ImageView flattylogo = (ImageView) findViewById(R.id.logoFlatty); //flatty logo
        TextView flattytitle = (TextView) findViewById(R.id.Flatty); //flatty title
        ImageView profilepic = (ImageView) findViewById(R.id.userImage); //user profile pic
        TextView title = (TextView) findViewById(R.id.pageTitle);
        ImageButton homebtn = (ImageButton) findViewById(R.id.ButtonHome);
        ImageButton matchesbtn = (ImageButton) findViewById(R.id.ButtonMatches);
        ImageButton profilebtn = (ImageButton) findViewById(R.id.ButtonProfile);
        ImageButton settingsbtn = (ImageButton) findViewById(R.id.ButtonSettings);
        Button imageButton = (Button) findViewById(R.id.imageButton);
        Button saveButton = (Button) findViewById(R.id.SaveButton);
        Button signOutButton = (Button) findViewById(R.id.signOutButton);
        Button DeactivateButton = (Button) findViewById(R.id.DeactivateButton);
        //Button socialButton = (Button) findViewById(R.id.socialButton);

        Details details = account.getDetails();
        System.out.println(details.toString());
        System.out.println(details.getExtraInfo());

        textDegree.setSelection(getSpinnerIndex(textDegree, details.getDegree()));
        dietRequirement.setSelection(getSpinnerIndex(dietRequirement, details.getDietRequirements()));
        textBudget.setSelection(getSpinnerIndex(textBudget, details.getWeeklyBudget()));
        textHobbies.setText(details.getExtraInfo());

        textFirstName.setText(account.getFirstName());

        try {
            datePicker.setText(makeDateString(details.getDateOfBirth().getDate(), details.getDateOfBirth().getMonth() + 1, details.getDateOfBirth().getYear() + 1900));
        } catch (Exception e) {
            e.printStackTrace();
        }


        datePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar calc = Calendar.getInstance();
                int date = calc.get(Calendar.DATE);
                int month = calc.get(Calendar.MONTH);
                int year = calc.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateProfileActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        try {
                            datePicker.setText(makeDateString(dayOfMonth, month+1, year));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, date);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        saveButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        String firstname = String.valueOf(textFirstName.getText());
                        String dateofbirth = String.valueOf(datePicker.getText());
                        String degree = String.valueOf(textDegree.getSelectedItem());
                        String dietRequirementsValue = String.valueOf(dietRequirement.getSelectedItem());
                        String budget = String.valueOf(textBudget.getSelectedItem());
                        String hobbies = String.valueOf(textHobbies.getText());

                        String[] dateofbirthData = dateofbirth.split(" ");
                        System.out.println(Arrays.toString(dateofbirthData));

                        int date = Integer.parseInt(dateofbirthData[1]);
                        int month = 1;
                        int year = Integer.parseInt(dateofbirthData[2]);
                        try {
                            month = getMonthNumber(dateofbirthData[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println(year);

                        Date dateDOB = new Date(year - 1900, month - 1, date + 1);
                        account.setFirstName(firstname);

                        account.getDetails().setDegree(degree);
                        account.getDetails().setDateOfBirth(dateDOB);
                        account.getDetails().setDietRequirements(dietRequirementsValue);
                        account.getDetails().setWeeklyBudget(budget);
                        account.getDetails().setExtraInfo(hobbies);

                        System.out.println("Account saving: " + account.toString());
                        System.out.println("Details saving: " + account.getDetails().toString());

                        dao.updateAccount(account);
                        detailDAO.updateDetails(account);
                        String email = account.getEmail();
                        account.setDetails(detailDAO.getDetails(account));
                        Intent intent1 = new Intent(UpdateProfileActivity.this, HomePageActivity.class);
                        intent1.putExtra("account", account);
                        startActivity(intent1);
                    }
                }
        );

        /*

        socialButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        String shareBody = "Your body is here";
                        String shareSub = "Your subject";
                        myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                        myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(myIntent, "Find user through:"));
                    }
                }
        );

         */

        signOutButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        //sign user out
                        //redirect to login page
                        Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );
        DeactivateButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        AlertDialog alert = new AlertDialog.Builder(UpdateProfileActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Delete Account")
                                .setMessage("Are you sure you want to delete your account?")
                                .setPositiveButton("Yes", (dialog, which) -> finish())
                                .setNegativeButton("No", (dialog, which) -> finish()).show();
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                            dao.removeAccount(account);
                            detailDAO.deleteDetails(account);
                            Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                        });
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v12 -> alert.dismiss());
                    }
                }
        );
        homebtn.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        //redirect to home (matching) page
                        Intent intent = new Intent(UpdateProfileActivity.this, HomePageActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        matchesbtn.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        //redirect to matches page
                        Intent intent = new Intent(UpdateProfileActivity.this, MatchesActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        profilebtn.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        //redirect to profile
                        Intent intent = new Intent(UpdateProfileActivity.this, UserProfileActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        settingsbtn.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        //redirect to update profile page
                        Intent intent = new Intent(UpdateProfileActivity.this, UpdateProfileActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
        );
        imageButton.setOnClickListener(v -> {
            Intent imagePicker = new Intent();
            imagePicker.setAction(Intent.ACTION_GET_CONTENT);
            imagePicker.setType("image/*");
            startActivityForResult(Intent.createChooser(imagePicker, "Select Image"), 200);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView profilepic = (ImageView) findViewById(R.id.userImage); //user profile pic
        if (requestCode == 200 && resultCode == RESULT_OK) {
            try {
                Bitmap image = getCorrectlyOrientedImage(getApplicationContext(), data.getData(), 500);
                profilepic.setImageBitmap(image);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imageBytes = outputStream.toByteArray();
                // account.setPhoto(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri, new String[]{ MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
        if (cursor == null || cursor.getCount() != 1) {
            return 90;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri, int maxWidth) throws IOException {
        InputStream imageInput = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options bitOp = new BitmapFactory.Options();
        bitOp.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageInput, null, bitOp);
        imageInput.close();

        int rotatedWidth;
        int rotatedHeight;

        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            System.out.println("Orientation if statement called");
            rotatedWidth = bitOp.outHeight;
            rotatedHeight = bitOp.outWidth;
        } else {
            System.out.println("Orientation else statement called");
            rotatedWidth = bitOp.outWidth;
            rotatedHeight = bitOp.outHeight;
        }

        Bitmap srcImage;
        imageInput = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > maxWidth || rotatedHeight > maxWidth) {
            System.out.println("If statement called");
            float widthRatio = ((float) rotatedWidth) / ((float) maxWidth);
            float heightRatio = ((float) rotatedHeight) / ((float) maxWidth);
            float maxRatio = Math.max(widthRatio, heightRatio);

            BitmapFactory.Options bitRatioOp = new BitmapFactory.Options();
            bitRatioOp.inSampleSize = (int) maxRatio;
            srcImage = BitmapFactory.decodeStream(imageInput, null, bitRatioOp);
        } else {
            srcImage = BitmapFactory.decodeStream(imageInput);
        }
        imageInput.close();

        System.out.println("Orientation: " + orientation);

        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcImage = Bitmap.createBitmap(srcImage, 0, 0, srcImage.getWidth(), srcImage.getHeight(), matrix, true);
        }

        return srcImage;
    }

    private String makeDateString(int dayOfMonth, int month, int year) throws Exception {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    private String getMonthFormat(int month) throws Exception {
        if (month == 1) {
            return "JAN";
        } else if (month == 2) {
            return "FEB";
        } else if (month == 3) {
            return "MAR";
        } else if (month == 4) {
            return "APR";
        } else if (month == 5) {
            return "MAY";
        } else if (month == 6) {
            return "JUN";
        } else if (month == 7) {
            return "JUL";
        } else if (month == 8) {
            return "AUG";
        } else if (month == 9) {
            return "SEP";
        } else if (month == 10) {
            return "OCT";
        } else if (month == 11) {
            return "NOV";
        } else if (month == 12) {
            return "DEC";
        } else {
            throw new Exception("Invalid Month Format");
        }
    }

    private int getMonthNumber(String month) throws Exception {
        if (month.equals("JAN")) {
            return 1;
        } else if (month.equals("FEB")) {
            return 2;
        } else if (month.equals("MAR")) {
            return 3;
        } else if (month.equals("APR")) {
            return 4;
        } else if (month.equals("MAY")) {
            return 5;
        } else if (month.equals("JUN")) {
            return 6;
        } else if (month.equals("JUL")) {
            return 7;
        } else if (month.equals("AUG")) {
            return 8;
        } else if (month.equals("SEP")) {
            return 9;
        } else if (month.equals("OCT")) {
            return 10;
        } else if (month.equals("NOV")) {
            return 11;
        } else if (month.equals("DEC")) {
            return 12;
        } else {
            throw new Exception("Invalid Month");
        }
    }

    private String getTodaysDate() throws Exception {
        Calendar calc = Calendar.getInstance();
        int year = calc.get(Calendar.YEAR);
        int month = calc.get(Calendar.MONTH);
        month++;
        int date = calc.get(Calendar.DATE);
        return makeDateString(date, month, year);
    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        int index = 0;

        for (int i =0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                index = i;
            }
        }
        return index;
    }

}