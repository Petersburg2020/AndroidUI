package nx.peter.app.android_ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import nx.peter.app.android_ui.R;

public class WindowLayout extends AView<WindowLayout> {
    protected LinearLayout toolBar;
    protected RecyclerView launchedApps;


    public WindowLayout(Context context) {
        super(context);
    }

    public WindowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.layout_window, this);



    }

    @Override
    protected void reset() {

    }


    public interface App {
        Tab getTab();
        <V extends View> Window<V> getWindow();
    }

    public interface Window<V extends View> extends IView<V> {
        <C extends View> C getContentView();
        boolean isEmpty();
        boolean isNotEmpty();
    }

    public interface Tab {
        Drawable getIcon();
        CharSequence getName();
        boolean isActiveTab();
    }

    protected static class ITab implements Tab {
        public CharSequence name;
        public Drawable icon;
        public boolean isActiveTab;

        public ITab(CharSequence name, Drawable icon) {
            this(name, icon, false);
        }

        public ITab(CharSequence name, Drawable icon, boolean isActiveTab) {
            this.name = name;
            this.icon = icon;
            this.isActiveTab = isActiveTab;
        }

        public void setName(CharSequence name) {
            this.name = name;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public void setActiveTab(boolean activeTab) {
            isActiveTab = activeTab;
        }

        @Override
        public Drawable getIcon() {
            return icon;
        }

        @Override
        public CharSequence getName() {
            return name;
        }

        @Override
        public boolean isActiveTab() {
            return isActiveTab;
        }
    }

}
