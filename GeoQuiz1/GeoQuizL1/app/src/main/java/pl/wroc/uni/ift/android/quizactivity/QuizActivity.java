package pl.wroc.uni.ift.android.quizactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    // Key for questions array to be stored in bundle;
    private static final String KEY_QUESTIONS = "questions";

    private static final int CHEAT_REQEST_CODE = 0;


    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;

    private TextView mQuestionTextView;

    private Button mCheatButton;

    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_stolica_polski, true),
            new Question(R.string.question_stolica_dolnego_slaska, false),
            new Question(R.string.question_sniezka, true),
            new Question(R.string.question_wisla, true)
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater = false;
    int odpowiedziane = 0;
    int poprawne = 0;

    //    Bundles are generally used for passing data between various Android activities.
    //    It depends on you what type of values you want to pass, but bundles can hold all
    //    types of values and pass them to the new activity.
    //    see: https://stackoverflow.com/questions/4999991/what-is-a-bundle-in-an-android-application
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");

        setTitle(R.string.app_name);
        // inflating view objects
        setContentView(R.layout.activity_quiz);

        // check for saved data
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            Log.i(TAG, String.format("onCreate(): Restoring saved index: %d", mCurrentIndex));

            // here in addition we are restoring our Question array;
            // getParcelableArray returns object of type Parcelable[]
            // since our Question is implementing this interface (Parcelable)
            // we are allowed to cast the Parcelable[] to desired type which
            // is the Question[] here.
            mQuestionsBank = (Question []) savedInstanceState.getParcelableArray(KEY_QUESTIONS);
            // sanity check
            if (mQuestionsBank == null)
            {
                Log.e(TAG, "Question bank array was not correctly returned from Bundle");

            } else {
                Log.i(TAG, "Question bank array was correctly returned from Bundle");
            }
            mIsCheater = savedInstanceState.getBoolean("ch", false);
            odpowiedziane = savedInstanceState.getInt("odp",0);
            poprawne = savedInstanceState.getInt("pop",0);
        }

        mCheatButton = (Button) findViewById(R.id.button_cheat);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean currentAnswer = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, currentAnswer);
//
//                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
//                boolean currentAnswer = mQuestionsBank[mCurrentIndex].isAnswerTrue();
//                intent.putExtra("answer", currentAnswer);

                startActivityForResult(intent, CHEAT_REQEST_CODE);
            }
        });


        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex == 0)
                    mCurrentIndex = mQuestionsBank.length - 1;
                else
                    mCurrentIndex = (mCurrentIndex - 1);
                updateQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == CHEAT_REQEST_CODE) {
            if (data != null)
            {
                boolean answerWasShown = CheatActivity.wasAnswerShown(data);
                if (answerWasShown) {
                    mIsCheater = true;
                    Toast.makeText(this,
                            R.string.message_for_cheaters,
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, String.format("onSaveInstanceState: current index %d ", mCurrentIndex) );

        //we still have to store current index to correctly reconstruct state of our app
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt("odp", odpowiedziane);
        savedInstanceState.putInt("pop", poprawne);
        savedInstanceState.putBoolean("ch",mIsCheater);
        // because Question is implementing Parcelable interface
        // we are able to store array in Bundle
        savedInstanceState.putParcelableArray(KEY_QUESTIONS, mQuestionsBank);
    }

    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if (odpowiedziane >= 4){
            if(mIsCheater==false){
                Toast.makeText(this, "KONIEC", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "KONIEC, oszuscie!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();

        int toastMessageId;

        if (userPressedTrue == answerIsTrue) {
            toastMessageId = R.string.correct_toast;
            poprawne++;

        } else {
            toastMessageId = R.string.incorrect_toast;
        }
        odpowiedziane++;
        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT).show();
    }
}
