package pl.wroc.uni.ift.android.quizactivity;


        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v7.app.AppCompatActivity;

        import static pl.wroc.uni.ift.android.quizactivity.R.id.fragment_quiz;
        import static pl.wroc.uni.ift.android.quizactivity.R.id.main_layout;

/**
 * Created by jpola on 07.08.17.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(main_layout);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(main_layout, fragment).commit();
        }
    }
}