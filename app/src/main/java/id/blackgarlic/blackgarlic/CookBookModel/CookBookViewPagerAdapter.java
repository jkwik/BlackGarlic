package id.blackgarlic.blackgarlic.CookBookModel;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import id.blackgarlic.blackgarlic.R;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by Justin Kwik on 08/07/2016.
 */
public class CookBookViewPagerAdapter extends PagerAdapter {

    public final String BLACKGARLIC_PICTURES = "http://bgmenu.kilatstorage.com/menu_id.jpg";

    Context mcontext;
    CookBookObject mCookBookObject;

    public CookBookViewPagerAdapter(Context context, CookBookObject cookBookObject) {
        this.mcontext = context;
        this.mCookBookObject = cookBookObject;
    }

    @Override
    public int getCount() {
        return mCookBookObject.getCookBookStepList().size() + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ScrollView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (position == 0) {
            View clickedOverviewView = LayoutInflater.from(mcontext).inflate(R.layout.clicked_overview, container, false);

            SimpleDraweeView cookBookClickedImage = (SimpleDraweeView) clickedOverviewView.findViewById(R.id.cookBookClickedImage);
            TextView cookBookClickedImageTitle = (TextView) clickedOverviewView.findViewById(R.id.cookBookClickedImageTitle);

            //Setting Image Work
            cookBookClickedImage.setImageURI(Uri.parse(BLACKGARLIC_PICTURES.replace("menu_id", String.valueOf(mCookBookObject.getMenu_id()))));

            //Setting Image Title Work with Fonts

            String menuTitle = mCookBookObject.getMenu_name();
            String menuSubname = mCookBookObject.getMenu_subname();

            CalligraphyTypefaceSpan robotoBold = new CalligraphyTypefaceSpan(TypefaceUtils.load(mcontext.getAssets(), "fonts/Roboto-Bold.ttf"));
            CalligraphyTypefaceSpan robotoThin = new CalligraphyTypefaceSpan(TypefaceUtils.load(mcontext.getAssets(), "fonts/Roboto-Thin.ttf"));

            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

            if (menuSubname.length() == 0) {
                stringBuilder.append(menuTitle);
                stringBuilder.setSpan(robotoBold, 0, menuTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                stringBuilder.append(menuTitle).append("\n" + menuSubname);
                stringBuilder.setSpan(robotoBold, 0, menuTitle.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.setSpan(robotoThin, menuTitle.length() + 1, menuTitle.length() + menuSubname.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            cookBookClickedImageTitle.setText(stringBuilder, TextView.BufferType.SPANNABLE);

            container.addView(clickedOverviewView);
            return clickedOverviewView;

        } else {

            return null;

        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ScrollView) object);
    }
}
