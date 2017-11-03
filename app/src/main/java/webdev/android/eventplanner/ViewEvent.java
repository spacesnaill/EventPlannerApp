package webdev.android.eventplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ViewEvent extends AppCompatActivity {

    TextView desc;
    TextView address;
    TextView date;
    TextView time;
    TextView RSVP;

    Button edit;
    Button delete;

    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        desc = (TextView) findViewById(R.id.desc);
        address = (TextView) findViewById(R.id.address);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        RSVP = (TextView) findViewById(R.id.rsvp);

        edit = (Button) findViewById(R.id.edit);
        delete = (Button) findViewById(R.id.delete);


        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("eventList");
        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");

        setTitle(eventId);

        query.whereEqualTo("eventName", eventId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    desc.append(" " + objects.get(0).get("eventDescription").toString());
                    address.append(" " + objects.get(0).get("combinedAddress").toString());
                    date.append(" " + objects.get(0).get("eventDate").toString());
                    time.append(" " + objects.get(0).get("eventTime").toString());
                    RSVP.append(" " + objects.get(0).get("RSVP").toString());

                }
                else{
                    Log.i("error", e.toString());
                }
            }
        });

        query.whereEqualTo("eventName", eventId);
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){

                    delete.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);

                }
                else{
                    //Log.i("error", e.toString());
                }
            }
        });

    }

    public void addRSVP(View view){

        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("eventList");

        query.whereEqualTo("eventName", eventId);
        query.whereNotEqualTo("whoVoted", ParseUser.getCurrentUser().getUsername());


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    int amountOfRSVPs = (Integer) objects.get(0).get("RSVP");
                    amountOfRSVPs = amountOfRSVPs + 1;
                    objects.get(0).put("RSVP", amountOfRSVPs);
                    objects.get(0).add("whoVoted", ParseUser.getCurrentUser().getUsername());


                    objects.get(0).saveInBackground();
                    Intent intent = new Intent(getApplicationContext(), EventList.class);
                    intent.putExtra("flag", "RSVPSuccess");
                    startActivity(intent);
                }
                else{
                    //Log.i("error", e.toString());
                    Toast.makeText(ViewEvent.this, "You're already RSVP'd.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void deleteEvent(View view){

        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("eventList");
        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");

        query.whereEqualTo("eventName", eventId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    objects.get(0).deleteInBackground();
                    Intent intent = new Intent(getApplicationContext(), EventList.class);
                    intent.putExtra("flag", "deleteSuccess");
                    startActivity(intent);

                }
                else{
                    Log.i("error", e.toString());
                }
            }
        });

    }

    public void editEvent(View view){

        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");

        intent = new Intent(getApplicationContext(), createEvent.class);
        intent.putExtra("flag", "editing");
        intent.putExtra("eventId", eventId);
        startActivity(intent);

    }

    public void goToMapActivity(View view){
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("eventList");
        query.whereEqualTo("eventName", eventId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    String fullAddress = objects.get(0).get("combinedAddress").toString();
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("address", fullAddress);
                    startActivity(intent);

                }
                else{
                    Log.i("error", e.toString());
                }
            }
        });




        ;
    }


}
