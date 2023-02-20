package nx.peter.app.android_ui.view.text;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.*;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.view.MultiActionView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpannableTextFormat {
	public static SpannableString mSpan;
	public static List<Text> texts;
	
	
	@NonNull
	public static SpanText getColorSpan(SpannableString span, CharSequence text, int color, @NonNull CharSequence... subs) {
		List<TextColor> colors = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			colors.add(new TextColor(sub, color, start));
		}
		
		return getColorSpan(span, text, colors);
	}
	
	@NonNull
	public static SpanText getColorSpan(SpannableString span, CharSequence text, List<TextColor> colors){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(TextColor color : colors)
			if(!Objects.equals(color.text, "") && color.start > -1 && txt.substring(color.start).contains(color.text) && color.color != -1){
				texts.add(color);
				mSpan.setSpan(new ForegroundColorSpan(color.color), color.start, color.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}


	@NonNull
	public static SpanText getBackgroundSpan(SpannableString span, CharSequence text, int color, @NonNull CharSequence... subs) {
		List<TextBackground> colors = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			colors.add(new TextBackground(sub, color, start));
		}
		return getBackgroundSpan(span, text, colors);
	}

	@NonNull
	public static SpanText getBackgroundSpan(SpannableString span, CharSequence text, List<TextBackground> colors){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(TextBackground background : colors)
			if(!Objects.equals(background.text, "") && background.start > -1 && txt.substring(background.start).contains(background.text) && background.color != -1){
				texts.add(background);
				mSpan.setSpan(new BackgroundColorSpan(background.color), background.start, background.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}

	
	@NonNull
	public static SpanText getFontSpan(SpannableString span, CharSequence text, Font font, @NonNull CharSequence... subs) {
		List<TextFont> fonts = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			fonts.add(new TextFont(sub, font, start));
		}
		return getFontSpan(span, text, fonts);
	}
	
	@NonNull
	public static SpanText getFontSpan(SpannableString span, CharSequence text, List<TextFont> fonts){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(TextFont font : fonts)
			if(!Objects.equals(font.text, "") && font.start > -1 && txt.substring(font.start).contains(font.text) && font.font != null)
				if(font.font.get() != null){
					texts.add(font);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
						mSpan.setSpan(new TypefaceSpan(font.font.get()), font.start, font.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
		return new SpanText(span, texts);
	}
	

	@NonNull
	public static SpanText getLinkSpan(SpannableString span, CharSequence text, MultiActionView.OnLinkClickListener<?> link, @NonNull CharSequence... subs) {
		List<TextLink> links = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			links.add(new TextLink(sub, link, start));
		}
		return getLinkSpan(span, text, links);
	}
	
	@NonNull
	public static SpanText getLinkSpan(SpannableString span, final CharSequence text, List<TextLink> links){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(final TextLink link : links)
			if(!Objects.equals(link.text, "") && link.start > -1 && txt.substring(link.start).contains(link.text) && link.link != null) {
				texts.add(link);
					mSpan.setSpan(new ClickableSpan() {
							@Override
							public void onClick(View v) {
								if(link.view != null)
									link.link.onClickLink(link.view, text, link);
							}
				}, link.start, link.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}
	

	@NonNull
	public static SpanText getSizeSpan(SpannableString span, CharSequence text, int size, @NonNull CharSequence... subs) {
		List<TextSize> sizes = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			sizes.add(new TextSize(sub, size, start));
		}
		return getSizeSpan(span, text, sizes);
	}

	@NonNull
	public static SpanText getSizeSpan(SpannableString span, CharSequence text, List<TextSize> sizes){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();

		for(TextSize size : sizes)
			if(!Objects.equals(size.text, "") && size.start > -1 && txt.substring(size.start).contains(size.text) && size.size != -1){
				texts.add(size);
				mSpan.setSpan(new AbsoluteSizeSpan(size.size, true), size.start, size.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}


	@NonNull
	public static SpanText getUrlSpan(SpannableString span, CharSequence text, CharSequence url, @NonNull CharSequence... subs) {
		List<TextURL> urls = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			urls.add(new TextURL(sub, url, start));
		}
		return getUrlSpan(span, text, urls);
	}

	@NonNull
	public static SpanText getUrlSpan(SpannableString span, CharSequence text, List<TextURL> urls){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(TextURL url : urls)
			if(!Objects.equals(url.text, "") && url.start > -1 && txt.substring(url.start).contains(url.text) && url.url != null){
				texts.add(url);
				mSpan.setSpan(new URLSpan(url.url), url.start, url.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}


	@NonNull
	public static SpanText getUnderlineSpan(SpannableString span, CharSequence text, @NonNull CharSequence... subs) {
		List<TextUnderline> underlines = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			underlines.add(new TextUnderline(sub, start));
		}
		return getUnderlineSpan(span, text, underlines);
	}

	@NonNull
	public static SpanText getUnderlineSpan(SpannableString span, CharSequence text, List<TextUnderline> underlines){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(TextUnderline underline : underlines)
			if(!Objects.equals(underline.text, "") && underline.start > -1 && txt.substring(underline.start).contains(underline.text)){
				texts.add(underline);
				mSpan.setSpan(new UnderlineSpan(), underline.start, underline.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}


	@NonNull
	public static SpanText getSubscriptSpan(SpannableString span, CharSequence text, @NonNull CharSequence... subs) {
		List<TextSubscript> underlines = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			underlines.add(new TextSubscript(sub, start));
		}
		return getSubscriptSpan(span, text, underlines);
	}

	@NonNull
	public static SpanText getSubscriptSpan(SpannableString span, CharSequence text, List<TextSubscript> subscripts){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(TextSubscript subscript : subscripts)
			if(!Objects.equals(subscript.text, "") && subscript.start > -1 && txt.substring(subscript.start).contains(subscript.text)){
				texts.add(subscript);
				mSpan.setSpan(new SubscriptSpan(), subscript.start, subscript.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}


	@NonNull
	public static SpanText getStrikeThroughSpan(SpannableString span, CharSequence text, @NonNull CharSequence... subs) {
		List<TextStrikeThrough> strikeThroughs = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			strikeThroughs.add(new TextStrikeThrough(sub, Color.BLACK, start));
		}
		return getStrikeThroughSpan(span, text, strikeThroughs);
	}

	@NonNull
	public static SpanText getStrikeThroughSpan(SpannableString span, CharSequence text, int color, @NonNull CharSequence... subs) {
		List<TextStrikeThrough> strikeThroughs = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			strikeThroughs.add(new TextStrikeThrough(sub, color, start));
		}
		return getStrikeThroughSpan(span, text, strikeThroughs);
	}
	
	@NonNull
	public static SpanText getStrikeThroughSpan(SpannableString span, CharSequence text, List<TextStrikeThrough> strikeThroughs){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(TextStrikeThrough strikeThrough : strikeThroughs)
			if(!Objects.equals(strikeThrough.text, "") && strikeThrough.start > -1 && txt.substring(strikeThrough.start).contains(strikeThrough.text)){
				texts.add(strikeThrough);
				mSpan.setSpan(new StrikethroughSpan(), strikeThrough.start, strikeThrough.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				mSpan.setSpan(new ForegroundColorSpan(strikeThrough.color), strikeThrough.start, strikeThrough.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}

	
	@NonNull
	public static SpanText getSuperscriptSpan(SpannableString span, CharSequence text, @NonNull CharSequence... subs) {
		List<TextSuperscript> superscripts = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			superscripts.add(new TextSuperscript(sub, start));
		}
		return getSuperscriptSpan(span, text, superscripts);
	}

	@NonNull
	public static SpanText getSuperscriptSpan(SpannableString span, CharSequence text, List<TextSuperscript> superscripts){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(TextSuperscript superscript : superscripts)
			if(!Objects.equals(superscript.text, "") && superscript.start > -1 && txt.substring(superscript.start).contains(superscript.text)){
				texts.add(superscript);
				mSpan.setSpan(new SuperscriptSpan(), superscript.start, superscript.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}

	
	@NonNull
	public static SpanText getImageSpan(SpannableString span, CharSequence text, Context context, int image, float size, @NonNull CharSequence... subs) {
		List<TextImage> images = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			images.add(new TextImage(sub, context, image, size, start));
		}
		return getImageSpan(span, text, images);
	}

	@NonNull
	public static SpanText getImageSpan(SpannableString span, CharSequence text, Context context, @DrawableRes int image, @ColorInt int tint, float size, @NonNull CharSequence... subs) {
		List<TextImage> images = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			images.add(new TextImage(sub, context, image, tint, size, start));
		}
		return getImageSpan(span, text, images);
	}

	@NonNull
	public static SpanText getImageSpan(SpannableString span, CharSequence text, List<TextImage> colors){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(TextImage image : colors)
			if(!Objects.equals(image.text, "") && image.start > -1 && txt.substring(image.start).contains(image.text) && image.image != null){
				if (image.size < 6)
					image.size = 6;
				texts.add(image);
				float scale = image.size/image.image.getIntrinsicHeight();
				image.image.setBounds(0, 0, (int) (scale * image.image.getIntrinsicWidth()), (int) (scale * image.image.getIntrinsicHeight()));
				mSpan.setSpan(new ImageSpan(image.image, ImageSpan.ALIGN_BASELINE), image.start, image.end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}



	@NonNull
	public static SpanText getBulletSpan(SpannableString span, CharSequence text, int gap, int color, int radius, @NonNull CharSequence... subs) {
		List<TextBullet> bullets = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			bullets.add(new TextBullet(sub, gap, color, radius, start));
		}
		return getBulletSpan(span, text, bullets);
	}
	
	@NonNull
	public static SpanText getBulletSpan(SpannableString span, CharSequence text, @NonNull CharSequence... subs) {
		List<TextBullet> bullets = new ArrayList<>();
		for(CharSequence sub : subs) {
			int start = text.toString().indexOf(sub.toString());
			bullets.add(new TextBullet(sub, start));
		}
		return getBulletSpan(span, text, bullets);
	}

	@NonNull
	public static SpanText getBulletSpan(SpannableString span, CharSequence text, List<TextBullet> bullets){
		if(span != null)
			mSpan = span;
		else
			mSpan = new SpannableString(text);
		texts = new ArrayList<>();
		String txt = text.toString();
		for(TextBullet bullet : bullets)
			if(!Objects.equals(bullet.text, "") && bullet.start > -1 && txt.substring(bullet.start).contains(bullet.text)){
				texts.add(bullet);
				BulletSpan bull = new BulletSpan();
				if (bullet.color != -1 && bullet.gap != -1 && bullet.radius != -1)
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
						bull = new BulletSpan(bullet.gap, bullet.color, bullet.radius);
					}
				if (bullet.color != -1 && bullet.gap != -1 && bullet.radius == -1)
					bull = new BulletSpan(bullet.gap, bullet.color);
				if (bullet.color == -1 && bullet.gap != -1 && bullet.radius == -1)
					bull = new BulletSpan(bullet.gap);
				mSpan.setSpan(bull, bullet.start, bullet.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		return new SpanText(span, texts);
	}
	
	
	
	public static class SpanText {
		public final SpannableString span;
		public final List<Text> texts;
		
		public SpanText(SpannableString span, List<Text> texts) {
			this.span = span;
			this.texts = texts;
		}
		
	}


}
