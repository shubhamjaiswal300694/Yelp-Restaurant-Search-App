/*package*/
package edu.cmu.andrew.sgjaiswa;

/*imports*/
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "Project4Task1Servlet",
        // The servlet allows two URL patterns in the web browser
        // to see what url patter is present in the url bar and do appropriate work when the url listed below appears.
        urlPatterns = {"/getResults"})

/*This is the servlet class that is used to get an input from the android application. This input is then used to be
* appended in the api url and make a connection to the Yelp website. The api returns a JSON object which is then parsed
* using a JSONParser. This data is later fetched by the Android app by making a connection to the servlet using the Heroku app*/
public class Project4Task1Servlet extends javax.servlet.http.HttpServlet {

    /*To get the parameter from the android app and fetch the results in JSON format*/
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws IOException {

        /*get the parameter*/
        String searchTag = request.getParameter("location");
        StringBuilder r = new StringBuilder();
        /*a url to make a connection*/
        String yelpURL = "https://api.yelp.com/v3/businesses/search?location="+searchTag+"&limit=1";
        URL url = new URL(yelpURL);
        /*create connection*/
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        /*Yelp api authorization*/
        connection.addRequestProperty("Authorization","Bearer CchxYyqaK2tmyW3UmZZXrHP48yMHb_M4eCoVT4tA4CdUef7kla1HNlPGfptaIXg68uM28CwHsoLHfuca3ZwXNTtDWCZPAERI6-THPo8PAC44X6thoLFvWl3RxIezXXYx");
        // Read all the text returned by the server
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String str;
        // Read each line of "in" until done, adding each to r
        while ((str = in.readLine()) != null) {
            // str is one line of text readLine() strips newline characters
            r.append(str);
        }
        in.close();

        /*parse the string returned by the website in JSON*/
        JSONParser parser = new JSONParser();
        JSONObject j = null;
        try {
            j = (JSONObject) parser.parse(r.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*get only the requred data from the returned JSON*/
        JSONArray businesses = (JSONArray) j.get("businesses");
        /*get only the data associated with the business and store in a JSON object*/
        JSONObject biz = (JSONObject) businesses.get(0);
        JSONObject sendJSON = new JSONObject();
        /*add all the required data in the JSON object*/
        sendJSON.put("name",biz.get("name"));
        sendJSON.put("image",biz.get("image_url"));
        sendJSON.put("contact",biz.get("display_phone"));
        sendJSON.put("rating",biz.get("rating"));
        sendJSON.put("review",biz.get("review_count"));
        sendJSON.put("url",biz.get("url"));

        /*see the sample output*/
        /*PrintWriter out = response.getWriter();
        out.println(sendJSON);*/
    }
}
