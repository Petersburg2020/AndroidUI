package nx.peter.app.android_ui.view.text;

import android.content.Context;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.view.util.Random;

import java.util.List;

public class FontFamily {
  protected final Context c;
  protected List<FontDetail> details;
  protected final CharSequence name;

  private FontFamily(@NonNull CharSequence name, @NonNull Context c) {
    this.name = name;
    this.c = c;
  }

  private void addMember(@NonNull Style style, @NonNull Font font) {
    if (!contains(style))
      details.add(new IFontDetail(style, font));
  }

  private boolean contains(@NonNull Style style) {
    for (FontDetail d : details) if (d.getStyle().equals(style)) return true;
    return false;
  }

  public Font getFont(@NonNull Style style) {
    for (FontDetail d : details) if (d.getStyle().equals(style)) return d.getFont();
    return null;
  }

  public static FontFamily customize(Font regular) {
    FontFamily fam = new FontFamily(getName(Family.Custom), regular.context);
    for (Style s : Style.values()) fam.addMember(s, regular.setStyle(toFontStyle(s)));
    return fam;
  }

  public static FontFamily create(Family family, Context c) {
    FontFamily fam = new FontFamily(getName(family), c);
    Font font;
    switch (family) {
      case Custom:
        break;
      case SansSerif:
        for (Style s : Style.values()) {
          font = Font.SANS_SERIF;
          fam.addMember(s, font.setStyle(toFontStyle(s)));
        }
        break;
      case Serif:
        for (Style s : Style.values()) {
          font = Font.SERIF;
          fam.addMember(s, font.setStyle(toFontStyle(s)));
        }
        break;
      case JosefinSans:
        font = Random.nextInt(1, 2) == 1 ? Font.SANS_SERIF : Font.SERIF;
        for (Style s : Style.values()) {
          switch (s) {
            case Bold:
              font = Font.JOSEFIN_SANS_BOLD;
              break;
            case BoldItalic:
              font = Font.JOSEFIN_SANS_BOLD_ITALIC;
              break;
            case Light:
              font = Font.JOSEFIN_SANS_LIGHT;
              break;
            case LightItalic:
              font = Font.JOSEFIN_SANS_LIGHT_ITALIC;
              break;
            case Regular:
              font = Font.JOSEFIN_SANS_REGULAR;
              break;
            case SemiBold:
              font = Font.JOSEFIN_SANS_SEMI_BOLD;
              break;
            case SemiBoldItalic:
              font = Font.JOSEFIN_SANS_SEMI_BOLD_ITALIC;
              break;
            case Thin:
              font = Font.JOSEFIN_SANS_EXTRA_BOLD;
              break;
            case ThinItalic:
              font = Font.JOSEFIN_SANS_EXTRA_BOLD_ITALIC;
          }
          fam.addMember(s, font);
        }
        break;
      default:
        for (Style s : Style.values()) {
          font = Random.nextInt(1, 2) == 1 ? Font.SANS_SERIF : Font.SERIF;
          fam.addMember(s, getFont(family, s, font.setStyle(toFontStyle(s))));
        }
    }
    return fam;
  }

  private static Font getFont(Family fam, Style style, Font fallback) {
    Font temp = fallback;
    String path = "font/" + fam + "-" + style + ".ttf";
    temp.setAsset(path);
    return temp;
  }

  public static String getName(Family fam) {
    return fam.toString();
  }

  public static Font.Style toFontStyle(@NonNull Style style) {
    switch (style) {
      case Bold:
      case SemiBold:
        return Font.Style.Bold;
      case LightItalic:
      case ThinItalic:
        return Font.Style.Italic;
      case BoldItalic:
      case SemiBoldItalic:
        return Font.Style.BoldItalic;
      default:
        return Font.Style.Regular;
    }
  }

  public enum Style {
    Light,
    LightItalic,
    Thin,
    ThinItalic,
    Regular,
    Italic,
    SemiBold,
    SemiBoldItalic,
    Bold,
    BoldItalic
  }

  public enum Family {
    JosefinSans,
    SansSerif,
    Serif,
    Custom
  }

  private static class IFontDetail implements FontDetail {
    Style style;
    Font font;

    public IFontDetail(Style style, Font font) {
      this.style = style;
      this.font = font;
    }

    @Override
    public Font getFont() {
      return font;
    }

    @Override
    public Style getStyle() {
      return style;
    }
  }

  public interface FontDetail {
    Style getStyle();

    Font getFont();
  }
}

