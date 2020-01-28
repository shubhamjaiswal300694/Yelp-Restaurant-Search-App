/*package*/
package edu.cmu.sgjaiswa;
/*imports*/
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

/*
 * This class provides capabilities to search for an restaurant on Yelp.com given a search term.  The method "search" is the entry to the class.
 * Network operations cannot be done from the UI thread, therefore this class makes use of an AsyncTask inner class that will do the network
 * operations in a separate worker thread.  However, any UI updates should be done in the UI thread so avoid any synchronization problems.
 * onPostExecution runs in the UI thread, and it calls the ImageView pictureReady method to do the update.
 *
 */
public class getRestaurant {
    MainActivity ma = null;
    /*
     * search is the public GetRestaurant method. Its arguments are the search term, and the MainActivity object that called it. This provides a callback
     * path such that the pictureReady method in that object is called when the restaurant is available from the search.
     */
    public void search(String searchTerm, MainActivity ma) {
        this.ma = ma;
        new AsyncRestaurantSearch().execute(searchTerm);
    }

    /*Async Class to keep the android app responsive when a search is made*/
    @SuppressLint("StaticFieldLeak")
    private class AsyncRestaurantSearch extends AsyncTask<String, Void, ReturnType> {

        /*run the method in background*/
        protected ReturnType doInBackground(String... urls) {
            return search(urls[0]);
        }

        /*after the background task is made*/
        protected void onPostExecute(ReturnType lst) {
            ma.pictureReady(lst);
        }

        /*get the search word and make a HTTP connection to the heroku app to return a ReturnType object*/
        private ReturnType search(String searchTerm) {

            /*check id the searchterm is emty*/
            if (!searchTerm.equals("")) {
                try {
                    /*url*/
                    String yelpURL = "https://secret-savannah-24775.herokuapp.com/getResults?location="+searchTerm;
                    URL url = new URL(yelpURL);
                    /*make url connection*/
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    StringBuilder r = new StringBuilder();
                    // Read all the text returned by the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    String str;
                    // Read each line of "in" until done, adding each to r
                    while ((str = in.readLine()) != null) {
                        // str is one line of text readLine() strips newline characters
                        r.append(str);
                    }
                    in.close();

                    /*create a json onject and store the string in returntype object*/
                    ReturnType ret = new ReturnType();
                    JSONObject json = new JSONObject(r.toString());
                    ret.name = (String) json.get("name");
                    ret.contact = (String) json.get("contact");
                    ret.url = (String) json.get("url");
                    ret.image = getRemoteImage(new URL((String) json.get("image")));
                    return ret;
                } catch (IOException | JSONException e) {
                    return new ReturnType();
                }
            } else {
                return new ReturnType();
            }
        }

        /*return image of the image urk returned by the api*/
        /*code taken from the interestinpicture lab*/
        private Bitmap getRemoteImage(final URL url) {
            try {
                final URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}