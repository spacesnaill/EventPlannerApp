package webdev.android.eventplanner;


import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseServer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //local datastore
        Parse.enableLocalDatastore(this);

        //initialization code
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext()).applicationId("aa243be61baa19e89d9194bd62590cff6954ca19").clientKey("32748b5912525abd4197a38e93ed1638bf8f1aed").server("http://ec2-52-38-186-103.us-west-2.compute.amazonaws.com:80/parse/").build());


        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
