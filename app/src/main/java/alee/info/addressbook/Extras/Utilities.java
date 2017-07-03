package alee.info.addressbook.Extras;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class Utilities {

    //To send an email
    public static void sendEmail(String to, Context context){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, "");
        email.putExtra(Intent.EXTRA_TEXT, "");
        email.setType("message/rfc822");
        context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    //To make a call
    public static void makeCall(String number, Context context){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        context.startActivity(callIntent);
    }
}
