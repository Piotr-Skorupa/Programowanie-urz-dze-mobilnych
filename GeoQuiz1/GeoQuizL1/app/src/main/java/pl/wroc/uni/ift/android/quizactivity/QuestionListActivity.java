package pl.wroc.uni.ift.android.quizactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class QuestionListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private QuestionBank mQuestionBank = QuestionBank.getInstance();
    private List<Question> dataList = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.Recycler);

        dataList = mQuestionBank.getQuestions();

        mRecyclerView.setHasFixedSize(true);

        // ustawiamy LayoutManagera
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ustawiamy animatora, który odpowiada za animację dodania/usunięcia elementów listy
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());



        // tworzymy adapter oraz łączymy go z RecyclerView
        mRecyclerView.setAdapter(new MyAdapter(dataList, mRecyclerView));
    }
}
