package pl.wroc.uni.ift.android.quizactivity;

/**
 * Created by jpola on 26.07.17.
 */

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean odpowiedziane;

    public Question(int textResId, boolean answerTrue)    {

        mTextResId=textResId;
        mAnswerTrue = answerTrue;
        odpowiedziane = false;
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

    public void zmienodp(){
        odpowiedziane = true;
    }
    public boolean czyodp (){
        return odpowiedziane;
    }
}
