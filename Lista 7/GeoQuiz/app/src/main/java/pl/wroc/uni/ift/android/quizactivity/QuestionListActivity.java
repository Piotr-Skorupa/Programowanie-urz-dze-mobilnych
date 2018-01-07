package pl.wroc.uni.ift.android.quizactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.w3c.dom.Text;

import java.util.List;

public class QuestionListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private QuestionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle(R.string.app_name);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        QuestionBank questionBank = QuestionBank.getInstance();
        mAdapter = new QuestionAdapter(questionBank.getQuestions());
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenu = getMenuInflater();
        mMenu.inflate(R.menu.my_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.add_question) {
            Toast.makeText(this, "Question has been added!", Toast.LENGTH_SHORT).show();
            mAdapter.addQuestionToList();
            mRecyclerView.setAdapter(mAdapter);
        }
        if(item.getItemId() == R.id.quit){
           System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    private class QuestionHolder extends RecyclerView.ViewHolder
    {
        private TextView mQuestionTextView;
        private TextView mQuestionIndexTextView;
        private Question mQuestion;

        public QuestionHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.question_item,parent,false));
            mQuestionTextView = (TextView) itemView.findViewById(R.id.questions_item);
            mQuestionIndexTextView = (TextView) itemView.findViewById(R.id.questions_item_numer);
        }

        public void bind(Question question, int position)
        {
            mQuestion = question;
            mQuestionTextView.setText(mQuestion.getTextResId());
            mQuestionIndexTextView.setText("#"+Integer.toString(position+1)+" ");
        }

    }
    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder>
    {
        private List<Question> mQuestions;
        public QuestionAdapter(List<Question> questions)
        {
            mQuestions = questions;
        }
        @Override
        public void onBindViewHolder(QuestionHolder holder, int position) {
            Question question = mQuestions.get(position);
            holder.bind(question,position);
        }
        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(QuestionListActivity.this);
            return new QuestionHolder(layoutInflater,parent);
        }
        @Override
        public int getItemCount() {return mQuestions.size();}

        public void addQuestionToList(){

            mQuestions.add(new Question( R.string.addedQuestion, true));
        }

    }

}

