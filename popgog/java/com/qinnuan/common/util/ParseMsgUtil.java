package com.qinnuan.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.showu.baogu.R;
import com.qinnuan.common.widget.Face;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseMsgUtil {

    public static String convertToMsg(CharSequence cs, Context mContext) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
        ImageSpan[] spans = ssb.getSpans(0, cs.length(), ImageSpan.class);
        for (int i = 0; i < spans.length; i++) {
            ImageSpan span = spans[i];
            String c = span.getSource();
            int a = ssb.getSpanStart(span);
            int b = ssb.getSpanEnd(span);
            if (c.contains("emoji")) {
                ssb.replace(a, b, convertUnicode(c));
            }
        }
        ssb.clearSpans();
        return ssb.toString();
    }

    public static String convertUnicode(String emo) {
        emo = emo.substring(emo.indexOf("_") + 1);
        if (emo.length() < 6) {
            return new String(Character.toChars(Integer.parseInt(emo, 16)));
        }
        String[] emos = emo.split("_");
        char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
        char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
        char[] emoji = new char[char0.length + char1.length];
        for (int i = 0; i < char0.length; i++) {
            emoji[i] = char0[i];
        }
        for (int i = char0.length; i < emoji.length; i++) {
            emoji[i] = char1[i - char0.length];
        }
        return new String(emoji);
    }

    public static SpannableStringBuilder convetToHtml(String content,
                                                      Context mContext) {
        String regex = "\\[e\\](.*?)\\[/e\\]";
        Pattern pattern = Pattern.compile(regex);
        String emo = "";
        Resources resources = mContext.getResources();
        String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
        Matcher matcher = pattern.matcher(unicode);
        // LogUtil.e(classes, msg)
        SpannableStringBuilder sBuilder = faceConvetToHtml(content, mContext);
        Drawable drawable = null;
        ImageSpan span = null;
        while (matcher.find()) {
            emo = matcher.group();
            try {
                drawable = ImageUtil.getDrawableByAssets(
                        "emoticon/emoji_"
                                + emo.substring(emo.indexOf("]") + 1,
                                emo.lastIndexOf("[")) + ".png", mContext);
                drawable.setBounds(0, 0, 24, 24);
                span = new ImageSpan(drawable);
                sBuilder.setSpan(span, matcher.start(), matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                break;
            }
        }
        return sBuilder;
    }

    public static SpannableStringBuilder faceConvetToHtml(String content,
                                                          Context mContext) {
        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        String emo = "";
        String assetsPath = "";
        Resources resources = mContext.getResources();
        String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
        Matcher matcher = pattern.matcher(unicode);
        SpannableStringBuilder sBuilder = new SpannableStringBuilder(unicode);
        List<Face> faces = FaceParser.readMap(mContext);
        Drawable drawable = null;
        ImageSpan span = null;
        while (matcher.find()) {
            emo = matcher.group();
            for (int i = 0; i < faces.size(); i++) {
                if (faces.get(i).key.equals(emo)) {
                    assetsPath = faces.get(i).value;
                    drawable = ImageUtil.getDrawableFromAssetFile(mContext, "Custom/" + assetsPath);
                    drawable.setBounds(0, 0, 24, 24);
                    span = new ImageSpan(drawable);
                    sBuilder.setSpan(span, matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }
        }
        return sBuilder;
    }

    public static String convetToNotice(String content, Context mContext) {
        return EmojiParser.getInstance(mContext).convertEmoji(content);
    }

    public static SpannableStringBuilder convetToHeart(
            SpannableStringBuilder content, final Context mContext) {
        ImageGetter imageGetter = new ImageGetter() {
            public Drawable getDrawable(String source) {
                Drawable d = mContext.getResources().getDrawable(
                        R.drawable.icon);
                d.setBounds(0, 0, 24, 24);
                return d;
            }
        };
        CharSequence cs1 = Html.fromHtml("<img src='heart'/>", imageGetter,
                null);
        content.append(cs1);
        return content;
    }

    public static CharSequence convetToEmo(String content, Context mContext) {
        if (content == null) {
            return "表情";
        }
        try {
            int index = Integer.parseInt(content.substring(
                    content.indexOf("/") + 1, content.indexOf(".")));
            // String[] emos =
            // mContext.getResources().getStringArray(R.array.nn_emo);
            // return emos[index-1];
            return null;
        } catch (Exception e) {
            return "表情";
        }
    }
}
