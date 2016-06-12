package id.blackgarlic.blackgarlic.CookBookModel;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import id.blackgarlic.blackgarlic.MainActivity;
import id.blackgarlic.blackgarlic.R;
import id.blackgarlic.blackgarlic.Voucher.VoucherObject;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by Justin Kwik on 10/06/2016.
 */
public class CookBookAdapter extends RecyclerView.Adapter<CookBookAdapter.MyCookBookViewHolder> {

    public final String BLACKGARLIC_PICTURES = "http://bgmenu.kilatstorage.com/menu_id.jpg";

    List<CookBookObject> cookBookList = new ArrayList<CookBookObject>();
    Context mContext;
    List<CookBookObject> fullCookBookList = CookBook.getCookBookObjectList();
    boolean lastOne = false;
    int amountRemovedOnLastOne = 0;

    public void addMenus() {

        new MyAsyncTask().execute();

    }

    public CookBookAdapter(List<CookBookObject> cookBookObjectList, Context context) {
        this.cookBookList = cookBookObjectList;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == fullCookBookList.size()) {
            return 2;
        } else if (position == cookBookList.size()) {
            return 1;
        } else if (position != cookBookList.size()) {
            return 0;
        } else {
            return 3;
        }

    }

    @Override
    public MyCookBookViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if ((viewType == 3 || viewType == 2)) {
            View emptyView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emptyrowcookbook, viewGroup, false);
            return new MyCookBookViewHolder(emptyView);
        } else if (viewType == 1) {
            //Inflate loading shit
            View progressView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cookbookloadingrow, viewGroup, false);
            return new MyCookBookViewHolder(progressView);
        } else {
            View menuView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cookbookrow, viewGroup, false);
            return new MyCookBookViewHolder(menuView);
        }

    }

    @Override
    public void onBindViewHolder(MyCookBookViewHolder myViewHolder, int position) {

        if (position != cookBookList.size()) {

            Uri uri = Uri.parse(BLACKGARLIC_PICTURES.replace("menu_id", String.valueOf(cookBookList.get(position).getMenu_id())));
            myViewHolder.cookBookImage.setImageURI(uri);

            int menuTitleLength = cookBookList.get(position).getMenu_name().length();
            int menuSubNameLength = cookBookList.get(position).getMenu_subname().length();

            String menuTitle = cookBookList.get(position).getMenu_name();
            String menuSubname = cookBookList.get(position).getMenu_subname();

            CalligraphyTypefaceSpan robotoMedium = new CalligraphyTypefaceSpan(TypefaceUtils.load(mContext.getAssets(), "fonts/Roboto-Medium.ttf"));
            CalligraphyTypefaceSpan robotoThin = new CalligraphyTypefaceSpan(TypefaceUtils.load(mContext.getAssets(), "fonts/Roboto-Thin.ttf"));

            SpannableStringBuilder menuTitleStringBuilder = new SpannableStringBuilder();

            if (menuSubNameLength != 0) {
                menuTitleStringBuilder.append(menuTitle).append("\n" + menuSubname);
                menuTitleStringBuilder.setSpan(robotoMedium, 0, menuTitleLength + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                menuTitleStringBuilder.setSpan(robotoThin, menuTitleLength + 1, menuTitleLength + menuSubNameLength + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                menuTitleStringBuilder.append(menuTitle);
                menuTitleStringBuilder.setSpan(robotoMedium, 0, menuTitleLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            myViewHolder.cookBookTextView.setText(menuTitleStringBuilder, TextView.BufferType.SPANNABLE);

        } else {

            if (lastOne == false) {

                addMenus();

            }

        }

    }

    @Override
    public int getItemCount() {
        return cookBookList.size() + 1;
    }

    public class MyCookBookViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView cookBookImage;
        public TextView cookBookTextView;

        public MyCookBookViewHolder(View itemView) {
            super(itemView);

            cookBookImage = (SimpleDraweeView) itemView.findViewById(R.id.cookBookImage);
            cookBookTextView = (TextView) itemView.findViewById(R.id.cookBookTextView);
        }
    }


    private class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("Size: ", String.valueOf(cookBookList.size()));

            Runnable runnable2Secs = new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            };

            Handler handler = new Handler();

            handler.postDelayed(runnable2Secs, (int) (Math.random() * 1500));

            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {

            int startingPositionForAddingIntoAdapterList = CookBook.getStartingPositionForAddingIntoAdapterList();

            if (startingPositionForAddingIntoAdapterList + 20 > fullCookBookList.size()) {

                Log.e("Before Starting: ", String.valueOf(startingPositionForAddingIntoAdapterList));

                for (int i = startingPositionForAddingIntoAdapterList; i < fullCookBookList.size(); i++) {
                    cookBookList.add(fullCookBookList.get(i));
                    amountRemovedOnLastOne++;
                }

                CookBook.setStartingPositionForAddingIntoAdapterList(CookBook.getStartingPositionForAddingIntoAdapterList() + (fullCookBookList.size() - startingPositionForAddingIntoAdapterList));

                Log.e("After Starting: ", String.valueOf(CookBook.getStartingPositionForAddingIntoAdapterList()));

                Log.e("Last One: ", String.valueOf(amountRemovedOnLastOne));

                lastOne = true;

                return null;

            } else {

                Log.e("Before Starting: ", String.valueOf(startingPositionForAddingIntoAdapterList));

                for (int i = startingPositionForAddingIntoAdapterList; i < startingPositionForAddingIntoAdapterList + 20; i++) {
                    cookBookList.add(fullCookBookList.get(i));
                }

                CookBook.setStartingPositionForAddingIntoAdapterList(CookBook.getStartingPositionForAddingIntoAdapterList() + 20);

                Log.e("After Starting: ", String.valueOf(CookBook.getStartingPositionForAddingIntoAdapterList()));

                lastOne = false;

                return null;
            }

        }
    }

}
