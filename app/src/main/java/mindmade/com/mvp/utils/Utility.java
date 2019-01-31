package mindmade.com.mvp.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Value Stream Technologies on 31-01-2019.
 */
public class Utility {


    public void hideKeyboard(Activity activity, View view) {

        //if (AppConstants.) Log.w(AppConstants.tag, "Called hideKeyboard");
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v == null) {
           // if (AppConstants.debug) Log.w(AppConstants.tag, "Called hideKayboard IF");
            v = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
