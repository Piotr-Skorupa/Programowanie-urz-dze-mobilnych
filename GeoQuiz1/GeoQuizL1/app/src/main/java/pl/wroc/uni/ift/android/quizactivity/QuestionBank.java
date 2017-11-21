package pl.wroc.uni.ift.android.quizactivity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr Skorupa on 2017-11-14.
 */

public class QuestionBank  {

    private static QuestionBank INSTANCE;
    private Question[] data;
    private List<Question> dataList = new ArrayList<>();


    private QuestionBank(){
        data = new Question[]{
                new Question(R.string.question_stolica_polski, true),
                new Question(R.string.question_stolica_dolnego_slaska, false),
                new Question(R.string.question_sniezka, true),
                new Question(R.string.question_wisla, true),
                new Question(R.string.question_stolica_polski, true),
                new Question(R.string.question_stolica_dolnego_slaska, false),
                new Question(R.string.question_sniezka, true),
                new Question(R.string.question_wisla, true),
                new Question(R.string.question_stolica_polski, true),
                new Question(R.string.question_stolica_dolnego_slaska, false),
                new Question(R.string.question_sniezka, true),
                new Question(R.string.question_wisla, true),
                new Question(R.string.question_stolica_polski, true),
                new Question(R.string.question_stolica_dolnego_slaska, false),
                new Question(R.string.question_sniezka, true),
                new Question(R.string.question_wisla, true),
                new Question(R.string.question_stolica_polski, true),
                new Question(R.string.question_stolica_dolnego_slaska, false),
                new Question(R.string.question_sniezka, true),
                new Question(R.string.question_wisla, true)
        };

        dataList.add(data[0]);
        dataList.add(data[1]);
        dataList.add(data[2]);
        dataList.add(data[3]);
        dataList.add(data[0]);
        dataList.add(data[1]);
        dataList.add(data[2]);
        dataList.add(data[3]);
        dataList.add(data[0]);
        dataList.add(data[1]);
        dataList.add(data[2]);
        dataList.add(data[3]);
        dataList.add(data[0]);
        dataList.add(data[1]);
        dataList.add(data[2]);
        dataList.add(data[3]);
        dataList.add(data[0]);
        dataList.add(data[1]);
        dataList.add(data[2]);
        dataList.add(data[3]);
        dataList.add(data[0]);
        dataList.add(data[1]);
        dataList.add(data[2]);
        dataList.add(data[3]);
    }
    private QuestionBank(QuestionBank that){
        this.data = that.data;
        this.dataList = that.dataList;
        that.data = null;
        that.dataList = null;
    }

    public static QuestionBank getInstance(){
        if(INSTANCE == null){
            INSTANCE = new QuestionBank();
        }
        return INSTANCE;
    }

    public Question getQuestion(int index){return data[index];}

    public int size() {return data.length;}

    public List<Question> getQuestions(){return dataList;}

    public Question[] getArray(){return data;}
}
