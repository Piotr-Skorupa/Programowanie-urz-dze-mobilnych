package pl.wroc.uni.ift.android.quizactivity;


import android.os.Bundle;

import android.support.v4.app.Fragment;

public class QuizActivity extends SingleFragmentActivity {



    //    Bundles are generally used for passing data between various Android activities.
    //    It depends on you what type of values you want to pass, but bundles can hold all
    //    types of values and pass them to the new activity.
    //    see: https://stackoverflow.com/questions/4999991/what-is-a-bundle-in-an-android-application
    @Override
    protected Fragment createFragment()
    {
        return new QuizFragment().newInstance(3);
    }




}



