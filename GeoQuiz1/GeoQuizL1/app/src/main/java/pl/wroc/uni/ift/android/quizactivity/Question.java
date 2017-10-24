package pl.wroc.uni.ift.android.quizactivity;

/**
 * Created by jpola on 26.07.17.
 */

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mIsAnswered;

    public Question(int textResId, boolean answerTrue, boolean isAnswered)    {

        mTextResId=textResId;
        mAnswerTrue = answerTrue;
        mIsAnswered = isAnswered;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isAnswered(){return mIsAnswered;}

    public void setAnswered(boolean x) {mIsAnswered = x;}
}
