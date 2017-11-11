package pl.wroc.uni.ift.android.quizactivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private final static String EXTRA_KEY_ANSWER = "Answer";
    private final static String KEY_QUESTION = "QUESTION";
    private final static String EXTRA_KEY_SHOWN = "Shown";
    TextView mTextViewAnswer;
    Button mButtonShow;


    boolean mAnswer;
    boolean isCheating = false;
    String txt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        if (savedInstanceState != null) {
            isCheating = savedInstanceState.getBoolean("wasShown");
            txt = savedInstanceState.getString("text");
        }
        mAnswer = getIntent().getBooleanExtra(EXTRA_KEY_ANSWER,false);

        mTextViewAnswer = (TextView) findViewById(R.id.text_view_answer);
        mTextViewAnswer.setText(txt);

        mButtonShow = (Button) findViewById(R.id.button_show_answer);
        mButtonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswer) {
                    txt = "Prawda";
                    mTextViewAnswer.setText(txt);
                } else {
                    txt = "Fałsz";
                    mTextViewAnswer.setText(txt);
                }
                isCheating = true;
                setAnswerShown(isCheating);

            }
        });

        setAnswerShown(false);
    }


    public static boolean wasAnswerShown(Intent data)
    {
        return data.getBooleanExtra(EXTRA_KEY_SHOWN, false);
    }

    public static Intent newIntent(Context context, boolean answerIsTrue)
    {

        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_KEY_ANSWER, answerIsTrue);
        return intent;

    }

    private void setAnswerShown (boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_KEY_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("wasShown", isCheating);
        savedInstanceState.putString("text", mTextViewAnswer.getText().toString());

        // etc.
    }




}
