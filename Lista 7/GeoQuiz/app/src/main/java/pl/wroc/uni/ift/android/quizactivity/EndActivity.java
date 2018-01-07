package pl.wroc.uni.ift.android.quizactivity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    private TextView tytul;
    private TextView ilosc;
    private TextView punkty;
    private TextView tokeny;
    private String pyt;
    private String pun;
    private String tok;

    private int tok_int;
    private int pun_int;
    private int pyt_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        tok_int = getIntent().getIntExtra("tokens", 3);
        pyt_int = getIntent().getIntExtra("questions", 0);
        pun_int = getIntent().getIntExtra("correct", 0);

        tytul = (TextView) findViewById(R.id.tytul);
        tytul.setTextColor(Color.MAGENTA);

        ilosc = (TextView) findViewById(R.id.ilosc_pytan);
        pyt = "Ilość pytań w grze: ";
        ilosc.setText(pyt+pyt_int);


        punkty = (TextView) findViewById(R.id.ilosc_punktow);
        pun = "Ilość poprawnych: ";
        punkty.setText(pun+pun_int);


        tokeny = (TextView) findViewById(R.id.ilosc_tokenow);
        tok = "Ilość pozostałych tokenów: ";
        tokeny.setText(tok+tok_int);


    }
}
