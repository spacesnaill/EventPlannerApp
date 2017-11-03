package webdev.android.eventplanner;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class createEvent extends AppCompatActivity {

    TextView eventTitle;
    TextView eventDesc;
    TextView eventAddress;
    TextView eventZip;
    TextView eventCity;
    TextView eventCountry;

    TimePicker eventTime;
    DatePicker eventDate;

    ParseObject eventList;

    Button submitButton;
    Button continueButton;
    Button backButton;

    String eventId;
    String objectId;
    boolean editingMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event2);

        editingMode = false;

        eventTitle = (TextView) findViewById(R.id.eventTitle);
        eventDesc = (TextView) findViewById(R.id.eventDesc);
        eventAddress = (TextView) findViewById(R.id.eventAddress);
        eventZip = (TextView) findViewById(R.id.eventZip);
        eventCity = (TextView) findViewById(R.id.eventCity);
        eventCountry = (TextView) findViewById(R.id.eventCountry);
        eventTime = (TimePicker) findViewById(R.id.eventTime);
        eventDate = (DatePicker) findViewById(R.id.eventDate);
        eventList = new ParseObject("eventList");

        submitButton = (Button) findViewById(R.id.eventSubmit);
        continueButton = (Button) findViewById(R.id.continueButton);
        backButton = (Button) findViewById(R.id.backButton);

        Intent intent = getIntent();
        String checkingFlag = intent.getStringExtra("flag");
        if(checkingFlag.matches("editing")){
            eventId = intent.getStringExtra("eventId");
            final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("eventList");
            setTitle("Editing: " + eventId);
            query.whereEqualTo("eventName", eventId);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null){

                        eventTitle.setText(objects.get(0).get("eventName").toString());
                        eventDesc.setText(objects.get(0).get("eventDescription").toString());
                        eventAddress.setText(objects.get(0).get("streetAddress").toString());
                        eventZip.setText(objects.get(0).get("postalCode").toString());
                        eventCity.setText(objects.get(0).get("city").toString());
                        eventCountry.setText(objects.get(0).get("country").toString());
                        objectId = objects.get(0).getObjectId();
                        editingMode = true;

                    }
                    else{
                        Log.i("error", e.toString());
                    }
                }
            });
        }
        else{

        }

    }

    public void continueButtonPress(final View view){

        String titleTemp = eventTitle.getText().toString();
        String descTemp = eventDesc.getText().toString();
        String addressTemp = eventAddress.getText().toString();
        String zipTemp = eventZip.getText().toString();
        String cityTemp = eventCity.getText().toString();
        String countryTemp = eventCountry.getText().toString();

        final String title = titleTemp;
        final String desc = descTemp;
        final String address = addressTemp;
        final String zip = zipTemp;
        final String city = cityTemp;
        final String country = countryTemp;


        if(editingMode){
            final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("eventList");

            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject eventList, ParseException e) {
                    if(e == null){

                        if(TextUtils.isEmpty(title)){
                            eventTitle.setError("Title cannot be empty");
                        }
                        else if(TextUtils.isEmpty(desc)){
                            eventDesc.setError("Description cannot be empty");
                        }
                        else if(TextUtils.isEmpty(address)){
                            eventAddress.setError("Address cannot be empty");
                        }
                        else if(TextUtils.isEmpty(zip)){
                            eventZip.setError("Postal Code cannot be empty");
                        }
                        else if(TextUtils.isEmpty(city)){
                            eventCity.setError("City cannot be empty");
                        }
                        else if(TextUtils.isEmpty(country)){
                            eventCountry.setError("Country cannot be empty");
                        }
                        else{
                            if(eventTitle.getVisibility() == view.VISIBLE){

                                eventList.put("streetAddress", address);
                                eventList.put("postalCode", zip);
                                eventList.put("city", city);
                                eventList.put("country", country);
                                eventList.put("eventName", title);
                                eventList.put("eventDescription", desc);

                                String addressCombined = "";
                                addressCombined = address + ", " + zip + ", " + city + ", " + country;
                                eventList.put("combinedAddress", addressCombined);

                                eventList.saveInBackground();

                                eventTitle.setVisibility(view.GONE);
                                eventDesc.setVisibility(view.GONE);
                                eventAddress.setVisibility(view.GONE);
                                eventZip.setVisibility(view.GONE);
                                eventCity.setVisibility(view.GONE);
                                eventCountry.setVisibility(view.GONE);
                                eventDate.setVisibility(view.VISIBLE);
                                backButton.setVisibility(view.VISIBLE);
                            }
                            else if(eventDate.getVisibility() == view.VISIBLE){
                                eventDate.setVisibility(view.INVISIBLE);
                                eventTime.setVisibility(view.VISIBLE);
                                continueButton.setVisibility(view.GONE);
                                submitButton.setVisibility(view.VISIBLE);
                            }
                        }

                    }
                    else{
                        Log.i("error", e.toString());
                    }
                }
            });
        }
        else {
            if (TextUtils.isEmpty(title)) {
                eventTitle.setError("Title cannot be empty");
            } else if (TextUtils.isEmpty(desc)) {
                eventDesc.setError("Description cannot be empty");
            } else if (TextUtils.isEmpty(address)) {
                eventAddress.setError("Address cannot be empty");
            } else if (TextUtils.isEmpty(zip)) {
                eventZip.setError("Postal Code cannot be empty");
            } else if (TextUtils.isEmpty(city)) {
                eventCity.setError("City cannot be empty");
            } else if (TextUtils.isEmpty(country)) {
                eventCountry.setError("Country cannot be empty");
            } else {
                if (eventTitle.getVisibility() == view.VISIBLE) {


                    eventList.put("streetAddress", address);
                    eventList.put("postalCode", zip);
                    eventList.put("city", city);
                    eventList.put("country", country);
                    eventList.put("eventName", title);
                    eventList.put("eventDescription", desc);

                    String addressCombined = "";
                    addressCombined = address + ", " + zip + ", " + city + ", " + country;
                    eventList.put("combinedAddress", addressCombined);

                    eventTitle.setVisibility(view.GONE);
                    eventDesc.setVisibility(view.GONE);
                    eventAddress.setVisibility(view.GONE);
                    eventZip.setVisibility(view.GONE);
                    eventCity.setVisibility(view.GONE);
                    eventCountry.setVisibility(view.GONE);
                    eventDate.setVisibility(view.VISIBLE);
                    backButton.setVisibility(view.VISIBLE);
                } else if (eventDate.getVisibility() == view.VISIBLE) {
                    eventDate.setVisibility(view.INVISIBLE);
                    eventTime.setVisibility(view.VISIBLE);
                    continueButton.setVisibility(view.GONE);
                    submitButton.setVisibility(view.VISIBLE);
                }
            }
        }

    }


    public void backButtonPress(View view){
        if(eventDate.getVisibility() == view.VISIBLE){

            eventTitle.setVisibility(view.VISIBLE);
            eventDesc.setVisibility(view.VISIBLE);
            eventAddress.setVisibility(view.VISIBLE);
            eventZip.setVisibility(view.VISIBLE);
            eventCity.setVisibility(view.VISIBLE);
            eventCountry.setVisibility(view.VISIBLE);

            backButton.setVisibility(view.GONE);
            eventDate.setVisibility(view.INVISIBLE);
        }
        else if(eventTime.getVisibility() == view.VISIBLE){
            continueButton.setVisibility(view.VISIBLE);
            submitButton.setVisibility(view.GONE);
            eventDate.setVisibility(view.VISIBLE);
            eventTime.setVisibility(view.GONE);
        }
    }

    public void submitEvent(View view){

        String time = "5";
        final String date = (eventDate.getMonth() + 1) + "/" + eventDate.getDayOfMonth() + "/" + eventDate.getYear();

        int hour = eventTime.getCurrentHour();
        int min = eventTime.getCurrentMinute();
        String format;

        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        time = hour + " : " + min + " " + format;

        final String actualTime = time;

        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if(editingMode){
            final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("eventList");

            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject eventList, ParseException e) {
                    if(e == null){

                        //eventList.put("user", ParseUser.getCurrentUser().getUsername());
                        eventList.put("eventTime", actualTime);
                        eventList.put("eventDate", date);
                        //eventList.add("whoVoted", "");
                        //eventList.put("RSVP", 0);

                        eventList.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("save", "success");
                                    Intent intent = new Intent(getApplicationContext(), EventList.class);
                                    intent.putExtra("flag", "creationSuccess");
                                    startActivity(intent);
                                } else {
                                    Log.i("save", "failure: " + e.toString());
                                    Toast.makeText(createEvent.this, "There was an error with the Database, please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Log.i("error", e.toString());
                    }
                }
            });
        }
        else {


            eventList.put("user", ParseUser.getCurrentUser().getUsername());
            eventList.put("eventTime", time);
            eventList.put("eventDate", date);
            eventList.add("whoVoted", "");
            eventList.put("RSVP", 0);

            eventList.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("save", "success");
                        Intent intent = new Intent(getApplicationContext(), EventList.class);
                        intent.putExtra("flag", "creationSuccess");
                        startActivity(intent);
                    } else {
                        Log.i("save", "failure: " + e.toString());
                        Toast.makeText(createEvent.this, "There was an error with the Database, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}
