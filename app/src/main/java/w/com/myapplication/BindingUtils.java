package w.com.myapplication;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static w.com.myapplication.baseClasses.UtilConstantKt.AMAZON_URL_PREFIX;


public class BindingUtils {

    @BindingAdapter("firstname")
    public static void firstname(TextView view, String text) {
        if (text != null && !text.isEmpty()) {
            text = text + " & Me";
            view.setText(text);
        }
    }

    @BindingAdapter("loadTeamImage")
    public static void loadTeamImage(CircularImageView view, String imageUrl) {
        String url = AMAZON_URL_PREFIX;
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String[] stringArray = imageUrl.split(",");
            url = stringArray[0];
            if (!stringArray[0].startsWith("http")) {
                url = AMAZON_URL_PREFIX + stringArray[0];
            }
            if (!stringArray[0].isEmpty()) {
                view.setBorderWidth(7f);
                view.setShadowRadius(7f);
            } else {
                view.setBorderWidth(0f);
                view.setShadowRadius(0f);
            }
        }
        Log.e("url", "url" + url);
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(view.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(url)
                .into(view);
    }


}
