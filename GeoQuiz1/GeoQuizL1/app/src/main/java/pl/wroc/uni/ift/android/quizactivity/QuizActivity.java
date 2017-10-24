package pl.wroc.uni.ift.android.quizactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {


    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;

    private TextView mQuestionTextView;

    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_stolica_polski, true, false),
            new Question(R.string.question_stolica_dolnego_slaska, false, false),
            new Question(R.string.question_sniezka, true, false),
            new Question(R.string.question_wisla, true, false)
    };


    private int mCurrentIndex = 0;
    private int mCorrectAnswers = 0;
    private boolean isEnd = false;

    //    Bundles are generally used for passing data between various Android activities.
    //    It depends on you what type of values you want to pass, but bundles can hold all
    //    types of values and pass them to the new activity.
    //    see: https://stackoverflow.com/questions/4999991/what-is-a-bundle-in-an-android-application
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        // inflating view objects
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAnswer(true);

                    }
                }
        );

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
                checkFinish();
            }
        });

        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex > 1){
                mCurrentIndex = (mCurrentIndex - 1);
                }
                else { mCurrentIndex = (mQuestionsBank.length -1);}
                updateQuestion();
                checkFinish();
            }
        });

        updateQuestion();
    }

    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    // sprawdzanie poprawnosci odpowiedzi
    private void checkAnswer(boolean userPressedTrue) {

        if (mQuestionsBank[mCurrentIndex].isAnswered()==false){
            boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();

            int toastMessageId = 0;

            if (userPressedTrue == answerIsTrue) {
                toastMessageId = R.string.correct_toast;
                mCorrectAnswers += 1;
            } else {
                toastMessageId = R.string.incorrect_toast;
            }

            changeBoolean();

            Toast myToast = Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT);
            myToast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            myToast.show();
        }
        else Toast.makeText(this, "Już odpowiedziałeś na to pytanie", Toast.LENGTH_LONG).show();
    }
    // koncowy komunikat wyswietlajacy liczbe punktów
    private void finalPoints(){


        if(isEnd == false){

            String toastMessageId = "Ilość poprawnych odpowiedzi: ";
            toastMessageId = toastMessageId + mCorrectAnswers;

            Toast myToast = Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT);
            myToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            myToast.show();
            isEnd = true;
        }

    }
    // ustawianie pytania jako odpowiedzianego
    private void changeBoolean() {

        mQuestionsBank[mCurrentIndex].setAnswered(true);

    }
    // funkcja sprawdzajaca czy na wszystkie pytania odpowiedzial user
    private void checkFinish() {
        int koniec = 0;
        for (int i= 0; i< mQuestionsBank.length; i++ ){
            if (mQuestionsBank[i].isAnswered() == true){
                koniec++;
            }
        }
        if (koniec == mQuestionsBank.length){
            finalPoints();

        }

    }

}
