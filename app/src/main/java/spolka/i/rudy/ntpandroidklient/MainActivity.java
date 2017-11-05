package spolka.i.rudy.ntpandroidklient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnMainLogin = findViewById(R.id.btnMainLogin);

        btnMainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLogingActivity = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intentLogingActivity);
            }
        });
    }
}
