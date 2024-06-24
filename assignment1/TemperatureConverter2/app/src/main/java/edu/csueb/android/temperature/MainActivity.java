package edu.csueb.android.temperature;

import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {
    private EditText text;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText) findViewById(R.id.editTextText);
    }
    // this method is called at button click because we assigned the name to
    // the "OnClick property" of the button
    public void onClick(View view) {
        if(view.getId() == R.id.button){
            RadioButton celsiusButton =
                    (RadioButton) findViewById(R.id.radioButton);
            RadioButton fahrenheitButton =
                    (RadioButton) findViewById(R.id.radioButton);
            if (text.getText().length() == 0) {
                Toast.makeText(this, "Please enter a valid number",
                        Toast.LENGTH_LONG).show();
                return;
            }
            float inputValue = Float.parseFloat(text.getText().toString());
            if (celsiusButton.isChecked()) {
                text.setText(String
                        .valueOf(ConverterUtil.convertFahrenheitToCelsius(inputValue)));
                celsiusButton.setChecked(false);
                fahrenheitButton.setChecked(true);
            } else {
                text.setText(String
                        .valueOf(ConverterUtil.convertCelsiusToFahrenheit(inputValue)));
                fahrenheitButton.setChecked(false);
                celsiusButton.setChecked(true);
            }
        }
    }
}



