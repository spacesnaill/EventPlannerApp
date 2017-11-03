package webdev.android.eventplanner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class EventList extends AppCompatActivity {

    LocationManager locationManager;

    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.i("Location", location.toString());

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("eventList");

        final ArrayList<String> events = new ArrayList<String>();


        final ListView eventList = (ListView) findViewById(R.id.eventList);

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, events);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewEvent.class);
                intent.putExtra("eventId", events.get(i));
                startActivity(intent);
            }

        });

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    for(ParseObject event : objects){
                        events.add(event.get("eventName").toString());
                    }

                    eventList.setAdapter(arrayAdapter);

                } else{
                    e.printStackTrace();
                }
            }
        });


        Intent intent = getIntent();
        String checkingFlag = intent.getStringExtra("flag");
        if(checkingFlag.matches("creationSuccess")){
            Toast.makeText(EventList.this, "Event successfully created.", Toast.LENGTH_SHORT).show();
        }
        else if(checkingFlag.matches("editSuccess")){
            Toast.makeText(EventList.this, "Event successfully edited.", Toast.LENGTH_SHORT).show();
        }
        else if(checkingFlag.matches("login")){
            Toast.makeText(EventList.this, "Welcome.", Toast.LENGTH_SHORT).show();
        }
        else if(checkingFlag.matches("RSVPSuccess")){
            Toast.makeText(EventList.this, "RSVP Successful.", Toast.LENGTH_SHORT).show();
        }
        else if(checkingFlag.matches("deleteSuccess")){
            Toast.makeText(EventList.this, "Event Deletion Successful.", Toast.LENGTH_SHORT).show();
        }

    }

    public void showCreateEventActivity(View view){
        Intent intent = new Intent(getApplicationContext(), createEvent.class);
        intent.putExtra("flag", "creating");
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.logout){
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_event, menu);
        return true;
    }
}
