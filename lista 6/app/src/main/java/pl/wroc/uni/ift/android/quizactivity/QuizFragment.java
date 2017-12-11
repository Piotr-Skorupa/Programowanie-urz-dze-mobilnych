package pl.wroc.uni.ift.android.quizactivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class QuizFragment extends Fragment {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mBackButton;


    private TextView mQuestionTextView;

    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_stolica_polski, true),
            new Question(R.string.question_stolica_dolnego_slaska, false),
            new Question(R.string.question_sniezka, true),
            new Question(R.string.question_wisla, true)
    };

    private int mCurrentIndex = 0;
    private int mQuestionId = 0;
    private int iloscpoprawnych = 0;
    private int iloscodp = 0;


    public static QuizFragment newInstance(int id)
    {
        Bundle args = new Bundle();
        args.putInt("questionID", id);
        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try {
            mQuestionId = getArguments().getInt("questionID");

        }
        catch(Exception e){
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_quiz, container, false);

        mQuestionTextView = (TextView) view.findViewById(R.id.question_text_view);

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });


        mTrueButton = (Button) view.findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAnswer(true);
                    }
                }
        );

        mFalseButton = (Button) view.findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) view.findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });

        mBackButton = (ImageButton) view.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });


        updateQuestion();

        // Inflate the layout for this fragment
        return view;
    }



    //    Bundles are generally used for passing data between various Android activities.
    //    It depends on you what type of values you want to pass, but bundles can hold all
    //    types of values and pass them to the new activity.
    //    see: https://stackoverflow.com/questions/4999991/what-is-a-bundle-in-an-android-application


    private void updateQuestion() {
        //int question = mQuestionsBank[mCurrentIndex].getTextResId();
       // mQuestionTextView.setText(question);

        int question = mQuestionId;
        mQuestionTextView.setText(question);

        for (int i =0 ; i < mQuestionsBank.length; i++){
            if (mQuestionsBank[i].getTextResId() == question) mCurrentIndex = i;
        }

    }

    private void checkAnswer(boolean userPressedTrue) {
        if(mQuestionsBank[mCurrentIndex].czyodp()==false){
            boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();

            int toastMessageId = 0;

            if (userPressedTrue == answerIsTrue) {
                toastMessageId = R.string.correct_toast;
                iloscpoprawnych+=1;
            } else {
                toastMessageId = R.string.incorrect_toast;
            }
            mQuestionsBank[mCurrentIndex].zmienodp();
            iloscodp += 1;
            Toast toast = Toast.makeText(getActivity(), toastMessageId, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.CENTER_VERTICAL, 0 , 0);
            toast.show();
        }
        if(iloscodp == 4){
            String napis = "Koniec! Poprawnych odpowiedzi: ";
            napis = napis + iloscpoprawnych;

            Toast.makeText(getActivity(), napis, Toast.LENGTH_LONG).show();

        }
    }

}
