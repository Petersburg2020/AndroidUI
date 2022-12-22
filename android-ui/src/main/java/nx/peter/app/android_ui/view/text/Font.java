package nx.peter.app.android_ui.view.text;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

public class Font {
	@SuppressLint("StaticFieldLeak")
	public static Font SANS_SERIF,
			SERIF,
			MONOSPACE,
			SANS_SERIF_BOLD,
			SANS_SERIF_ITALIC,
			SANS_SERIF_BOLD_ITALIC,
			SERIF_BOLD,
			SERIF_ITALIC,
			SERIF_BOLD_ITALIC,
			DUKAS_BOLD,
			DUKAS_REGULAR,
            BAROQUE_SCRIPT,
            BEFORE_THE_RAIN,
            BEFORE_THE_RAIN_SWATCHES,
            CANTERBURY,
            RIESLING,
            MARLBORO,
            JOSEFIN_SANS_REGULAR,
            JOSEFIN_SANS_LIGHT,
            JOSEFIN_SANS_BOLD,
            JOSEFIN_SANS_THIN,
            JOSEFIN_SANS_SEMI_BOLD,
            JOSEFIN_SANS_BOLD_ITALIC,
            JOSEFIN_SANS_LIGHT_ITALIC,
            JOSEFIN_SANS_THIN_ITALIC,
            JOSEFIN_SANS_SEMI_BOLD_ITALIC,
			DUKAS_SEMI_BOLD;
            

	protected static boolean running = false;
    
    public static boolean doneLoading() {
        return running;
    }

	public static void init(@NonNull Context context) {
		if (running) {
			SANS_SERIF = new Font(context, Typeface.SANS_SERIF);
			SERIF = new Font(context, Typeface.SERIF);
			MONOSPACE = new Font(context, Typeface.MONOSPACE);
			SERIF_BOLD = new Font(context, Typeface.SERIF).setStyle(Style.Bold);
			SERIF_ITALIC = new Font(context, Typeface.SERIF).setStyle(Style.Italic);
			SERIF_BOLD_ITALIC = new Font(context, Typeface.SERIF).setStyle(Style.BoldItalic);
			SANS_SERIF_BOLD = new Font(context, Typeface.SANS_SERIF).setStyle(Style.Bold);
			SANS_SERIF_ITALIC = new Font(context, Typeface.SANS_SERIF).setStyle(Style.Italic);
			SANS_SERIF_BOLD_ITALIC = new Font(context, Typeface.SANS_SERIF).setStyle(Style.BoldItalic);
            
            
            BAROQUE_SCRIPT = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/BaroqueScript.ttf"));
			BEFORE_THE_RAIN = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/BeforeTheRain.ttf"));
            BEFORE_THE_RAIN_SWATCHES = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/BeforeTheRainSwashes.ttf"));
			CANTERBURY = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/Canterbury.ttf"));
            MARLBORO = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/Marlboro.ttf"));
            RIESLING = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/Riesling.ttf"));
            
            JOSEFIN_SANS_BOLD = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/JosefinSans-Bold.ttf"));
			JOSEFIN_SANS_BOLD_ITALIC = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/JosefinSans-BoldItalic.ttf"));
            JOSEFIN_SANS_SEMI_BOLD = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/JosefinSans-SemiBoldItalic.ttf"));
			JOSEFIN_SANS_SEMI_BOLD_ITALIC = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/JosefinSans-SemiBoldItalic.ttf"));
			JOSEFIN_SANS_THIN = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/JosefinSans-Thin.ttf"));
            JOSEFIN_SANS_THIN_ITALIC = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/JosefinSans-ThinItalic.ttf"));
            JOSEFIN_SANS_LIGHT = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/JosefinSans-Light.ttf"));
            JOSEFIN_SANS_LIGHT = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/JosefinSans-LightItalic.ttf"));
            JOSEFIN_SANS_REGULAR = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/JosefinSans-Regular.ttf"));
           
            /*
			DUKAS_BOLD = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/DukasCF-Bold.otf"));
			DUKAS_REGULAR = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/DukasCF-Regular.otf"));
			DUKAS_SEMI_BOLD = new Font(context, Typeface.createFromAsset(context.getAssets(), "font/DukasCF-SemiBold.otf"));
            */
            
            Toast.makeText(context, "Finished loading fonts", Toast.LENGTH_SHORT).show();
		}
		running = true;
	}
	
	protected Typeface font;
	public final Context context;
	
	public Font(@NonNull Context c) {
		this(c, null);
	}
	
	public Font(@NonNull Font font) {
		this(font.context, font.font);
	}

	public Font(@NonNull Context c, @Nullable Typeface font) {
		this.font = font;
		this.context = c;
	}

	public Font set(@NonNull Typeface font) {
		this.font = font;
		return this;
	}

	public Font setAsset(@NonNull CharSequence path) {
		return set(Typeface.createFromAsset(context.getAssets(), path.toString()));
	}
	
	public Font setFile(@NonNull File file) {
		return set(Typeface.createFromFile(file));
	}

	public Font setFile(@NonNull CharSequence file) {
		return set(Typeface.createFromFile(file.toString()));
	}
	
	public Typeface get() {
		return font;
	}
	
	public Font setStyle(@NonNull Style style){
		if(font != null)
			switch(style){
				case Bold:
					set(Typeface.create(font, Typeface.BOLD));
					break;
				case BoldItalic:
					set(Typeface.create(font, Typeface.BOLD_ITALIC));
					break;
				case Italic:
					set(Typeface.create(font, Typeface.ITALIC));
					break;
				case Regular:
					set(Typeface.create(font, Typeface.NORMAL));
					break;
			}
		return this;
	}
	
	public Style getStyle(){
		if(font == null)
			return null;
		switch(font.getStyle()){
			case Typeface.BOLD:
				return Style.Bold;
			case Typeface.BOLD_ITALIC:
				return Style.BoldItalic;
			case Typeface.ITALIC:
				return Style.Italic;
			case Typeface.NORMAL:
			default:
				return Style.Regular;
		}
	}
	
	
	public enum Style {
		Bold,
		BoldItalic,
		Italic,
        Regular
	}
}
