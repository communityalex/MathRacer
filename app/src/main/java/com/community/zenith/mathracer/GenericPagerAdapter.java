package com.community.zenith.mathracer;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.security.InvalidParameterException;

/**
 * Created by Alex on 13/08/2016.
 */
public class GenericPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;
    private String[] titles;
    private int[] icons;
    private Context context;
    private Class<? extends Fragment> [] fragments;
    private Integer colour;

    public GenericPagerAdapter(FragmentManager fm, Context ctxt) {
        super(fm);
        this.context = ctxt;
    }

    public GenericPagerAdapter fragments(Class<? extends Fragment>... fragments){
        if (tabCount == 0){
            tabCount = fragments.length;
        }
        if (fragments.length != tabCount){
            throw new InvalidParameterException("");
        }
        this.fragments = fragments;
        return this;
    }

    public GenericPagerAdapter icons(int... icons){
        if (tabCount == 0){
            tabCount = icons.length;
        }
        if (fragments != null){
            if (icons.length != fragments.length){
                throw new InvalidParameterException("There must be one icon for each fragment.");
            }
        }
        this.icons = icons;
        return this;
    }

    public GenericPagerAdapter titles(String... titles){
        if (tabCount == 0){
            tabCount = titles.length;
        }
        if (fragments != null){
            if (titles.length != fragments.length){
                throw new InvalidParameterException("There must be one title for each fragment.");
            }
        }
        this.titles = titles;
        return this;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < fragments.length){
            try {
                return fragments[position].newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    /*public @Nullable <T> T getFragmentForPosition(int position, Class<T> type){
        String tag = makeFragmentName(, getItemId(position));
        return (T) getSupportFragmentManager().findFragmentByTag(tag);
    }*/

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null){
            return titles[position];
        } else if (icons != null){
            Drawable image = ContextCompat.getDrawable(context, icons[position]);
            if (colour != null){
                image.mutate().setColorFilter(ContextCompat.getColor(context, colour), PorterDuff.Mode.SRC_IN);
            }
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
        return "";
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public GenericPagerAdapter iconColour(int col) {
        this.colour = col;
        return this;
    }
}
