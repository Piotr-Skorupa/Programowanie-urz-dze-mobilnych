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
    private static final String KEY_TOKENS = "TokensCount";

    // Key for questions array to be stored in bundle;
    private static final String KEY_QUESTIONS = "questions";

    private static final int CHEAT_REQEST_CODE = 0;

    private int mCheatTokens = 3;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;

    private TextView mQuestionTextView;
    private TextView mAPITextView;

    private Button mCheatButton;
    private Button mQuestionListButton;

    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_stolica_polski, true),
            new Question(R.string.question_stolica_dolnego_slaska, false),
            new Question(R.string.question_sniezka, true),
            new Question(R.string.question_wisla, true)
    };

    private int wynik = 0;
    private int odpowiedziane = 0;
    private int mCurrentIndex = 0;

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
        }

        mCheatButton = (Button) findViewById(R.id.button_cheat);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean currentAnswer = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                boolean IsCheated =  mQuestionsBank[mCurrentIndex].getWasCheated();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, currentAnswer, IsCheated, mCheatTokens);
//
//                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
//                boolean currentAnswer = mQuestionsBank[mCurrentIndex].isAnswerTrue();
//                intent.putExtra("answer", currentAnswer);

                startActivityForResult(intent, CHEAT_REQEST_CODE);
            }
        });

        mQuestionListButton = (Button) findViewById(R.id.button_question_list);
        mQuestionListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this,QuestionListActivity.class);
                startActivity(intent);
            }
        });

        // set API Level textview
        mAPITextView = (TextView) findViewById(R.id.textView_API);
        mAPITextView.setText("API Level: "+Integer.valueOf(android.os.Build.VERSION.SDK_INT).toString());

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

                    Toast.makeText(this,
                            R.string.message_for_cheaters,
                            Toast.LENGTH_LONG)
                            .show();
                    mQuestionsBank[mCurrentIndex].setWasCheated(true);
                }

                mCheatTokens = CheatActivity.getCheatTokens(data);
                Log.d("Tokens result",Integer.toString(mCheatTokens));

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, String.format("onSaveInstanceState: current index %d ", mCurrentIndex) );

        //we still have to store current index to correctly reconstruct state of our app
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);

        // because Question is implementing Parcelable interface
        // we are able to store array in Bundle
        savedInstanceState.putParcelableArray(KEY_QUESTIONS, mQuestionsBank);
        savedInstanceState.putInt(KEY_TOKENS,mCheatTokens);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        mQuestionsBank=(Question []) savedInstanceState.getParcelableArray(KEY_QUESTIONS);
        mCheatTokens = savedInstanceState.getInt(KEY_TOKENS);
    }

    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();

        int toastMessageId;

        if (userPressedTrue == answerIsTrue) {
            toastMessageId = R.string.correct_toast;
            wynik ++;
        } else {
            toastMessageId = R.string.incorrect_toast;
        }
        odpowiedziane ++;
        checkFinish();
        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void checkFinish(){
        if (odpowiedziane >= 4){
            Intent intent = new Intent(QuizActivity.this,EndActivity.class);
            intent.putExtra("questions", odpowiedziane);
            intent.putExtra("correct", wynik);
            intent.putExtra("tokens", mCheatTokens);
            startActivity(intent);
        }
    }

}
