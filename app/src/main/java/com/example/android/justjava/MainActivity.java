package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private int quantity;
    private final int PRICE_PER_CUP = 5;
    private String name;
    private Toast toastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView quantityView = findViewById(R.id.quantity_text_view);
        quantity = Integer.valueOf(quantityView.getText().toString());

        toastMessage = new Toast(getApplicationContext());
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        int maxInt = 10;
        if(quantity>=maxInt){
            try{
                Log.i("MainActivity", "Trying to cancel Toast!");
                toastMessage.cancel();
                Log.i("MainActivity", "Managed to cancel Toast!");
            } catch (Exception e){
                Log.i("MainActivity", "CRAAAAAAASH: No previous toast!!!");
            }
            toastMessage = Toast.makeText(getApplicationContext(), getString(R.string.max_cups_order, maxInt), Toast.LENGTH_SHORT);
            toastMessage.show();
            return;
        } else {
            quantity++;
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if(quantity==1){
            try{
                Log.i("MainActivity", "Trying to cancel Toast!");
                toastMessage.cancel();
                Log.i("MainActivity", "Managed to cancel Toast!");
            } catch (Exception e){
                Log.i("MainActivity", "Crash: No previous toast!!!");
            }
            toastMessage = Toast.makeText(getApplicationContext(), getString(R.string.min_number_cups), Toast.LENGTH_SHORT);
            toastMessage.show();
            return;
        } else {
            quantity--;
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedCream = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCream.isChecked();
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);

        EditText nameEditText = findViewById(R.id.name_edit_text);
        name = nameEditText.getText().toString();

        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));//only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject_order, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
//        displayMessage(priceMessage);

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("geo:47.6, -122.3"));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }

//        Locale locale=Locale.getDefault();
//        Currency currency=Currency.getInstance(locale);
//        String symbol = currency.getSymbol();
//        String priceMessage = "Total: " + symbol + price;
//        String priceMessage = "Total: "+ "$" + price;
//        priceMessage = priceMessage + "\n" + "Thank you!";
    }

    /**
     * Create summary of the order.
     *
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate is whether or not the user wants chocolate topping
     * @param price of the order
     * @return text summary
     */

    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate){
        //using the xliff tags in XML - Mark message parts that should not be translated
        String orderSummary = getString(R.string.order_summary_name, name);
        orderSummary += "\n" + getString(R.string.add_whipped_cream) + " " + checkBoolean(hasWhippedCream);
        orderSummary += "\n" + getString(R.string.add_chocolate) + " " + checkBoolean(hasChocolate);
        orderSummary += "\n" + getString(R.string.order_quantity, quantity);
        orderSummary += String.format("\n" + getString(R.string.total_price_summary) + NumberFormat.getCurrencyInstance().format(price));
        orderSummary += "\n" + getString(R.string.thank_you);
        return orderSummary;
    }

    private String checkBoolean(boolean trueOrFalse){
        if(trueOrFalse){
            return getString(R.string.yes);
        }
        return getString(R.string.no);
    }

    /**
     * Calculates the price of the order.
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int newPricePerCup = PRICE_PER_CUP;
        if(hasWhippedCream){
            newPricePerCup += 1;
        }
        if(hasChocolate){
            newPricePerCup += 2;
        }
        return quantity * newPricePerCup;
    }

//    /**
//     * This method displays the given text on the screen.
//     */
//    public void displayMessage(String message){
//        TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);
//        orderSummaryTextView.setText(message);
//    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int nr) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + nr);
    }

    public void goToWeb(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        }
    }

    public void goToAlarm(View view){
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "JustJava Alarm");
//                .putExtra(AlarmClock.EXTRA_HOUR, 18)
//                .putExtra(AlarmClock.EXTRA_MINUTES, 05);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    public void goToCamera(View view){
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    public void goToPhone(View view){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel: 0040753033202"));
            if (intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
        }
    }

//    public void goToNote(View view){
//        Intent intent = new Intent(NoteIntents.ACTION_CREATE_NOTE)
//                .putExtra(NoteIntents.EXTRA_NAME, "JustJava Note")
//                .putExtra(NoteIntents.EXTRA_TEXT, "Testing 1 2 3");
//        if(intent.resolveActivity(getPackageManager()) != null){
//            startActivity(intent);
//        }
//    }

//    /**
//     * This method displays the given price on the screen.
//     */
//    private void displayPrice(int number) {
//        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
//        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
//    }

}
