/*package*/
package edu.cmu.sgjaiswa;

/*imports*/
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

/*this is the main app of the adnroid class that starts up the android*/
public class MainActivity extends AppCompatActivity {

    /*override the onCreate method that makes the app functional*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity ma = this;
        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = findViewById(R.id.submit);
        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText) findViewById(R.id.cityName)).getText().toString().replaceAll("\\P{L}","");
                getRestaurant gp = new getRestaurant();

                /*send the searchterm to the getRestaurant.java*/
                gp.search(searchTerm, ma); // Done asynchronously in another thread. It calls ip.pictureReady() in this thread when complete.

                /*set view*/
                ImageView pictureView = findViewById(R.id.restaurantImage);
                TextView restaurant = findViewById(R.id.restaurant);
                TextView phone = findViewById(R.id.contact);
                TextView url = findViewById(R.id.url);
                TextView imageNotFound = findViewById(R.id.imageNotFound);

                /*make the inital views invisible*/
                pictureView.setVisibility(View.INVISIBLE);
                restaurant.setVisibility(View.INVISIBLE);
                phone.setVisibility(View.INVISIBLE);
                url.setVisibility(View.INVISIBLE);
                imageNotFound.setVisibility(View.INVISIBLE);
            }
        });
    }

    /*
     * This is called by the GetPicture object when the picture is ready.  This allows for passing back the Bitmap picture for updating the ImageView
     */

    /*this is the method that is called by the asyncTask in getRestaurant.java class to set the view of the android app
    * to different views*/
    @SuppressLint("SetTextI18n")
    public void pictureReady(ReturnType picture) {

        /*get all the views in variables to be used later*/
        ImageView pictureView = findViewById(R.id.restaurantImage);
        TextView searchView = (EditText) findViewById(R.id.cityName);
        /*handle spaces, special characters and integers*/
        String term = searchView.getText().toString().replaceAll("[^\\p{Alpha} ]","");
        TextView restaurant = findViewById(R.id.restaurant);
        TextView phone = findViewById(R.id.contact);
        TextView url = findViewById(R.id.url);
        TextView imageNotFound = findViewById(R.id.imageNotFound);

        /*check if the entered city is null*/
        /*set the view accodingly*/
        if (term.length()==0) {
            imageNotFound.setVisibility(View.VISIBLE);
            imageNotFound.setText("Sorry, Please enter a city!");
            pictureView.setImageResource(R.mipmap.ic_launcher);
            pictureView.setVisibility(View.INVISIBLE);
            restaurant.setVisibility(View.INVISIBLE);
            phone.setVisibility(View.INVISIBLE);
            url.setVisibility(View.INVISIBLE);
            /*if a city is entred set the views accordingly*/
        } else {
            pictureView.setImageBitmap(picture.image);
            pictureView.setVisibility(View.VISIBLE);
            restaurant.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            url.setVisibility(View.VISIBLE);
            imageNotFound.setVisibility(View.VISIBLE);
            imageNotFound.setText("Here is a restaurant you could go in the city of "+term);
            restaurant.setText(picture.name);
            phone.setText("Contact Number: "+picture.contact);
            url.setText("Restaurant URL: "+picture.url);
        }
        /*after a search has been made. CLear the searchArea.*/
        searchView.setText("");
        pictureView.invalidate();
    }
}
