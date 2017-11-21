package pl.wroc.uni.ift.android.quizactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
    private TextView mApiLevel;
    private String mLevelString;
    private TextView mTokenTextView;

    private Button mCheatButton;
    private Button mAllQuestionsButton;


    //próba singletona
    private QuestionBank mQuestionsBank = QuestionBank.getInstance();


    private int mCurrentIndex = 0;
    private boolean mIsCheater = false;
    private int odpowiedziane = 0;
    private int poprawne = 0;

    //tokeny
    private int tokeny = 3;
    private String tokenString;



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

            // mQuestionsBank = savedInstanceState.getParcelable(KEY_QUESTIONS);

            // sanity check
            if (mQuestionsBank.getArray() == null)
            {
                Log.e(TAG, "Question bank array was not correctly returned from Bundle");

            } else {
                Log.i(TAG, "Question bank array was correctly returned from Bundle");
            }
            mIsCheater = savedInstanceState.getBoolean("ch");
            odpowiedziane = savedInstanceState.getInt("odp");
            poprawne = savedInstanceState.getInt("pop");
            tokeny = savedInstanceState.getInt("tokens");
        }

        mApiLevel = (TextView) findViewById(R.id.Level);
        mLevelString = "API Level: "+ Build.VERSION.SDK_INT;
        mApiLevel.setText(mLevelString);

        mTokenTextView = (TextView) findViewById(R.id.token_view);
        updateTokens();

        mCheatButton = (Button) findViewById(R.id.button_cheat);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean currentAnswer = mQuestionsBank.getQuestion(mCurrentIndex).isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, currentAnswer);
                intent.putExtra("tokens", tokeny);
//                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
//                boolean currentAnswer = mQuestionsBank[mCurrentIndex].isAnswerTrue();
//                intent.putExtra("answer", currentAnswer);

                startActivityForResult(intent, CHEAT_REQEST_CODE);
            }
        });

        mAllQuestionsButton = (Button) findViewById(R.id.button_list);
        mAllQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, QuestionListActivity.class);

                startActivity(intent);

            }
        });


        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.size();
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
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.size();
                updateQuestion();
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex == 0)
                    mCurrentIndex = mQuestionsBank.size() - 1;
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
                    tokeny -=1;
                    updateTokens();
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
        savedInstanceState.putInt("tokens", tokeny);
        // because Question is implementing Parcelable interface
        // we are able to store array in Bundle

        // savedInstanceState.putParcelable(KEY_QUESTIONS, (Parcelable) mQuestionsBank);
    }

    private void updateQuestion() {
        int question = mQuestionsBank.getQuestion(mCurrentIndex).getTextResId();
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
        boolean answerIsTrue = mQuestionsBank.getQuestion(mCurrentIndex).isAnswerTrue();

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
    private void updateTokens(){
        tokenString ="Your tokens to cheat: "+ tokeny;
        if (tokeny == 0){
            mTokenTextView.setTextColor(Color.RED);
        }else {
            mTokenTextView.setTextColor(Color.GREEN);
        }
        mTokenTextView.setText(tokenString);
    }
}
