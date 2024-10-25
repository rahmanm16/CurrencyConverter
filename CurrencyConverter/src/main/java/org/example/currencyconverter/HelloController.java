package org.example.currencyconverter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HelloController {
    // CurrOne, CurrTwo - will hold currency tickers (from & to)
    // apiKey.txt holds api key to use currencyApi
    private String currOne, currTwo, apiKey;

    private ArrayList<String> currencyList;

    @FXML
    private TextField enterAmountField;

    @FXML
    private ComboBox<String> currencyOneBox, currencyTwoBox;

    @FXML
    private Label resultLabel;

    // retrieve api  - public void initialize
    //getApiKey();
    public void initialize() {
        getApiKey();
        try {
            // Retrieve & store listed currencies
            currencyList = loadCurrencyList();

            // Store list within combo boxes
            ObservableList<String> options = FXCollections.observableArrayList(currencyList);
            currencyOneBox.setItems(options);
            currencyTwoBox.setItems(options);
        } catch (IOException e) {
            System.out.println("Error: failure in retrieving currency list " + e);
        }
    }


    //retrieve currency list thru api
    //try catch
    // retrieve and store list of currencies - currList
    // priv array list loadCurr throws IOExce (refer to api docu)

    //Store api key
    // reader.readLine()

    // get api key - private void getapikey -
    // buffered reader
    // try catch finally
    // io exception
    // String file path

    public void setCurrOne() {
        currOne = currencyOneBox.getValue();
    }

    public void setCurrTwo() {
        currTwo = currencyTwoBox.getValue();
    }

    private void getApiKey() {
        BufferedReader reader = null;
        try {
            // Obtain file path of api key
            String filePath = getClass().getResource("/resources/apiKey.txt").getPath()
                    .replaceAll("%20", " ");
            reader = new BufferedReader(new FileReader(filePath));
            // Store api key
            apiKey = reader.readLine();

        } catch (IOException e) {
            System.out.println("Error Message: " + e);
        } finally {
            // close reader object to allocate resources
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                System.out.println("Error Message: " + e);
            }
        }
    }

    // Retrieve currency list via api call
    private ArrayList<String> loadCurrencyList() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.apilayer.com/currency_data/list")
                .addHeader("apikey", apiKey)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(response.body().charStream(), JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        ArrayList<String> currencyList = new ArrayList<>();
        for (String currency : jsonObject.getAsJsonObject("currencies").keySet()) {
            currencyList.add(currency);
        }
        return currencyList;
    }

    public void convertCurrency() throws IOException {
        if (enterAmountField.getText().equals("") || enterAmountField.getText() == null) return;
        if (currOne == null || currTwo == null) return;

        float conversionRate = getConversionRate();


        // Next, is to calculate the converted currency...
        float conversionResult = Float.parseFloat(enterAmountField.getText()) * conversionRate;

        // Display the converted result...
        resultLabel.setText(conversionResult + " " + currTwo);

    }


    // get conversion rate of currency...
    private float getConversionRate() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.apilayer.com/currency_data/live?source=" + currOne + "&currencies=" + currTwo)
                .addHeader("apikey", apiKey)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();


        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(response.body().charStream(), JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // Return conversion rate
        String key = currOne + currTwo;
        return Float.parseFloat(jsonObject.getAsJsonObject("quotes").get(key).getAsString());
    }
}