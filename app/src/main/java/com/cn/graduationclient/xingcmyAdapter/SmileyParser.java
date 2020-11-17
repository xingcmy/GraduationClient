package com.cn.graduationclient.xingcmyAdapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.cn.graduationclient.R;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmileyParser {
    private static SmileyParser sInstance;

    public static SmileyParser getInstance() {
        return sInstance;
    }

    public static void init(Context context) {
        sInstance = new SmileyParser(context);
    }

    private final Context mContext;
    private final String[] mSmileyTexts;
    private final Pattern mPattern;
    private final HashMap<String, Integer> mSmileyToRes;

    private SmileyParser(Context context) {
        mContext = context;
        mSmileyTexts = mContext.getResources().getStringArray(
                DEFAULT_SMILEY_TEXTS);
        mSmileyToRes = buildSmileyToRes();
        mPattern = buildPattern();
    }

    static class Smileys {
        // 表情图片集合
        private static final int[] sIconIds = {
                R.drawable.f000, R.drawable.f001, R.drawable.f002, R.drawable.f003, R.drawable.f004,R.drawable.f005,R.drawable.f006,R.drawable.f007,R.drawable.f008,R.drawable.f009,
                R.drawable.f010, R.drawable.f011, R.drawable.f012, R.drawable.f013, R.drawable.f014,R.drawable.f015,R.drawable.f016,R.drawable.f017,R.drawable.f018,R.drawable.f019,
                R.drawable.f020, R.drawable.f021, R.drawable.f022, R.drawable.f023, R.drawable.f024,R.drawable.f025,R.drawable.f026,R.drawable.f027,R.drawable.f028,R.drawable.f029,
                R.drawable.f030, R.drawable.f031, R.drawable.f032, R.drawable.f033, R.drawable.f034,R.drawable.f035,R.drawable.f036,R.drawable.f037,R.drawable.f038,R.drawable.f039,
                R.drawable.f040, R.drawable.f041, R.drawable.f042, R.drawable.f043, R.drawable.f044,R.drawable.f045,R.drawable.f046,R.drawable.f047,R.drawable.f048,R.drawable.f049,
                R.drawable.f050, R.drawable.f051, R.drawable.f052, R.drawable.f053, R.drawable.f054,R.drawable.f055,R.drawable.f056,R.drawable.f057,R.drawable.f058,R.drawable.f059,
                R.drawable.f060, R.drawable.f061, R.drawable.f062, R.drawable.f063, R.drawable.f064,R.drawable.f065,R.drawable.f066,R.drawable.f067,R.drawable.f068,R.drawable.f069,
                R.drawable.f070, R.drawable.f071, R.drawable.f072, R.drawable.f073, R.drawable.f074,R.drawable.f075,R.drawable.f076,R.drawable.f077,R.drawable.f078,R.drawable.f079,
                R.drawable.f080, R.drawable.f081, R.drawable.f082, R.drawable.f083, R.drawable.f084,R.drawable.f085,R.drawable.f086,R.drawable.f087,R.drawable.f088,R.drawable.f089,
                R.drawable.f090, R.drawable.f091, R.drawable.f092, R.drawable.f093, R.drawable.f094,R.drawable.f095,R.drawable.f096,R.drawable.f097,R.drawable.f098,R.drawable.f099,
                R.drawable.f100, R.drawable.f101, R.drawable.f102, R.drawable.f103, R.drawable.f104,R.drawable.f105,R.drawable.f106};
        // 将图片映射为 文字
        public static int f000 = 0,f001 = 1,f002 = 2,f003 = 3,f004 = 4,f005 = 5,f006 = 6,f007 = 7,f008 = 8,f009 = 9;
        public static int f010 = 10,f011 = 11,f012 = 12,f013 = 13,f014 = 14,f015 = 15,f016 = 16,f017 = 17,f018 = 18,f019 = 19;
        public static int f020 = 20,f021 = 21,f022 = 22,f023 = 23,f024 = 24,f025 = 25,f026 = 26,f027 = 27,f028 = 28,f029 = 29;
        public static int f030 = 30,f031 = 31,f032 = 32,f033 = 33,f034 = 34,f035 = 35,f036 = 36,f037 = 37,f038 = 38,f039 = 39;
        public static int f040 = 40,f041 = 41,f042 = 42,f043 = 43,f044 = 44,f045 = 45,f046 = 46,f047 = 47,f048 = 48,f049 = 49;
        public static int f050 = 50,f051 = 51,f052 = 52,f053 = 53,f054 = 54,f055 = 55,f056 = 56,f057 = 57,f058 = 58,f059 = 59;
        public static int f060 = 60,f061 = 61,f062 = 62,f063 = 63,f064 = 64,f065 = 65,f066 = 66,f067 = 67,f068 = 68,f069 = 69;
        public static int f070 = 70,f071 = 71,f072 = 72,f073 = 73,f074 = 74,f075 = 75,f076 = 76,f077 = 77,f078 = 78,f079 = 79;
        public static int f080 = 80,f081 = 81,f082 = 82,f083 = 83,f084 = 84,f085 = 85,f086 = 86,f087 = 87,f088 = 88,f089 = 89;
        public static int f090 = 90,f091 = 91,f092 = 92,f093 = 93,f094 = 94,f095 = 95,f096 = 96,f097 = 97,f098 = 98,f099 = 99;
        public static int f100 = 100,f101 = 101,f102 = 102,f103 = 103,f104 = 104,f105 = 105,f106 = 106;
        // 得到图片表情 根据id
        public static int getSmileyResource(int which) {
            return sIconIds[which];
        }
    }

    public static final int[] DEFAULT_SMILEY_RES_IDS = {
            Smileys.getSmileyResource(Smileys.f000), Smileys.getSmileyResource(Smileys.f001), Smileys.getSmileyResource(Smileys.f002), Smileys.getSmileyResource(Smileys.f003), Smileys.getSmileyResource(Smileys.f004),
            Smileys.getSmileyResource(Smileys.f005), Smileys.getSmileyResource(Smileys.f006), Smileys.getSmileyResource(Smileys.f007), Smileys.getSmileyResource(Smileys.f008), Smileys.getSmileyResource(Smileys.f009),
            Smileys.getSmileyResource(Smileys.f010), Smileys.getSmileyResource(Smileys.f011), Smileys.getSmileyResource(Smileys.f012), Smileys.getSmileyResource(Smileys.f013), Smileys.getSmileyResource(Smileys.f014),
            Smileys.getSmileyResource(Smileys.f015), Smileys.getSmileyResource(Smileys.f016), Smileys.getSmileyResource(Smileys.f017), Smileys.getSmileyResource(Smileys.f018), Smileys.getSmileyResource(Smileys.f019),
            Smileys.getSmileyResource(Smileys.f020), Smileys.getSmileyResource(Smileys.f021), Smileys.getSmileyResource(Smileys.f022), Smileys.getSmileyResource(Smileys.f023), Smileys.getSmileyResource(Smileys.f024),
            Smileys.getSmileyResource(Smileys.f025), Smileys.getSmileyResource(Smileys.f026), Smileys.getSmileyResource(Smileys.f027), Smileys.getSmileyResource(Smileys.f028), Smileys.getSmileyResource(Smileys.f029),
            Smileys.getSmileyResource(Smileys.f030), Smileys.getSmileyResource(Smileys.f031), Smileys.getSmileyResource(Smileys.f032), Smileys.getSmileyResource(Smileys.f033), Smileys.getSmileyResource(Smileys.f034),
            Smileys.getSmileyResource(Smileys.f035), Smileys.getSmileyResource(Smileys.f036), Smileys.getSmileyResource(Smileys.f037), Smileys.getSmileyResource(Smileys.f038), Smileys.getSmileyResource(Smileys.f039),
            Smileys.getSmileyResource(Smileys.f040), Smileys.getSmileyResource(Smileys.f041), Smileys.getSmileyResource(Smileys.f042), Smileys.getSmileyResource(Smileys.f043), Smileys.getSmileyResource(Smileys.f044),
            Smileys.getSmileyResource(Smileys.f045), Smileys.getSmileyResource(Smileys.f046), Smileys.getSmileyResource(Smileys.f047), Smileys.getSmileyResource(Smileys.f048), Smileys.getSmileyResource(Smileys.f049),
            Smileys.getSmileyResource(Smileys.f050), Smileys.getSmileyResource(Smileys.f051), Smileys.getSmileyResource(Smileys.f052), Smileys.getSmileyResource(Smileys.f053), Smileys.getSmileyResource(Smileys.f054),
            Smileys.getSmileyResource(Smileys.f055), Smileys.getSmileyResource(Smileys.f056), Smileys.getSmileyResource(Smileys.f057), Smileys.getSmileyResource(Smileys.f058), Smileys.getSmileyResource(Smileys.f059),
            Smileys.getSmileyResource(Smileys.f060), Smileys.getSmileyResource(Smileys.f061), Smileys.getSmileyResource(Smileys.f062), Smileys.getSmileyResource(Smileys.f063), Smileys.getSmileyResource(Smileys.f064),
            Smileys.getSmileyResource(Smileys.f065), Smileys.getSmileyResource(Smileys.f066), Smileys.getSmileyResource(Smileys.f067), Smileys.getSmileyResource(Smileys.f068), Smileys.getSmileyResource(Smileys.f069),
            Smileys.getSmileyResource(Smileys.f070), Smileys.getSmileyResource(Smileys.f071), Smileys.getSmileyResource(Smileys.f072), Smileys.getSmileyResource(Smileys.f073), Smileys.getSmileyResource(Smileys.f074),
            Smileys.getSmileyResource(Smileys.f075), Smileys.getSmileyResource(Smileys.f076), Smileys.getSmileyResource(Smileys.f077), Smileys.getSmileyResource(Smileys.f078), Smileys.getSmileyResource(Smileys.f079),
            Smileys.getSmileyResource(Smileys.f080), Smileys.getSmileyResource(Smileys.f081), Smileys.getSmileyResource(Smileys.f082), Smileys.getSmileyResource(Smileys.f083), Smileys.getSmileyResource(Smileys.f084),
            Smileys.getSmileyResource(Smileys.f085), Smileys.getSmileyResource(Smileys.f086), Smileys.getSmileyResource(Smileys.f087), Smileys.getSmileyResource(Smileys.f088), Smileys.getSmileyResource(Smileys.f089),
            Smileys.getSmileyResource(Smileys.f090), Smileys.getSmileyResource(Smileys.f091), Smileys.getSmileyResource(Smileys.f092), Smileys.getSmileyResource(Smileys.f093), Smileys.getSmileyResource(Smileys.f094),
            Smileys.getSmileyResource(Smileys.f095), Smileys.getSmileyResource(Smileys.f096), Smileys.getSmileyResource(Smileys.f097), Smileys.getSmileyResource(Smileys.f098), Smileys.getSmileyResource(Smileys.f099),
            Smileys.getSmileyResource(Smileys.f100), Smileys.getSmileyResource(Smileys.f101), Smileys.getSmileyResource(Smileys.f102), Smileys.getSmileyResource(Smileys.f103), Smileys.getSmileyResource(Smileys.f104),
            Smileys.getSmileyResource(Smileys.f105), Smileys.getSmileyResource(Smileys.f106)};

    public static final int DEFAULT_SMILEY_TEXTS = R.array.smiley_array;
    private HashMap<String, Integer> buildSmileyToRes() {
        if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {
            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }
        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(
                mSmileyTexts.length);
        for (int i = 0; i < mSmileyTexts.length; i++) {
            smileyToRes.put(mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);
        }
        return smileyToRes;
    }

    // 构建正则表达式
    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);
        patternString.append('(');
        for (String s : mSmileyTexts) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString
                .length(), ")");
        return Pattern.compile(patternString.toString());
    }

    // 根据文本替换成图片
    public CharSequence strToSmiley(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            Drawable drawable = mContext.getResources().getDrawable(resId);
            drawable.setBounds(0, 0, 100, 100);//这里设置图片的大小
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            builder.setSpan(imageSpan, matcher.start(),
                    matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }
}
