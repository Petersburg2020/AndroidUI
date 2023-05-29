package nx.peter.app.android_ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextTabView extends AView<TextTabView> {
	protected StyledText layout;
	protected OnTabClickListener listener;
	protected Adapter.OnDataChangedListener dListener;
	protected IAdapter adapter;
	protected List<TabColor> colors;
	protected int tabColor;
	protected boolean isAllTab;
	protected TabStyle style;
	protected Font.Style fontStyle;
	protected Font font;
	protected float size;
	

	public TextTabView(@NonNull Context context) {
		super(context);
	}
	
	public TextTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public void setTabStyle(@NonNull TabStyle style) {
		this.style = style;
		setup();
	}
	
	@Override
	protected void init(@Nullable AttributeSet attrs) {
		inflate(getContext(), R.layout.view_tab_text, this);
		layout = findViewById(R.id.layout);
		reset();


	}

	protected void reset() {
		tabColor = Color.WHITE;
		size = 16; //layout.getTextSize();
		layout.setText("");
		layout.setPadding(20);
		layout.setTextColor(tabColor);
		colors = new ArrayList<>();
		isAllTab = false;
		style = TabStyle.PartialLink;
		font = layout.getFont();
		layout.setVerticalAlignment(StyledView.VerticalAlignment.Center);
		fontStyle = font.getStyle();

		adapter = new IAdapter(listener);

		dListener = adapter -> setup();
		// setTextSize(16);

		setTabTexts("nx", "peter", "app", "mathapi");
		setTabDivider(Divider.Slash);
		setBackground(Background.Black);
	}
	
	private void setup() {
		layout.setText(adapter.getText());
		layout.setTextSize(size);
		layout.setFontStyle(fontStyle);
		
		int total = adapter.getCount();
		int start = 0;
		switch (style) {
			case PartialLink:
				total -= 1;
				break;
			case MonoLink:
				start = total - 1;
				break;
			case NoLink:
				total = 0;
		}
		
		if (!style.equals(TabStyle.NoLink)) {
			for (int pos = start; pos < total; pos++) {
				final int position = pos;
				layout.addLinks((view, text, link) -> {
					if (listener != null)
						listener.onTabClicked(TextTabView.this, getTab(position), position);
				}, getTab(pos).getLink());
			}
		}
		
		for (TabColor tab : colors)
			layout.addSubColors(tab.color, tab.text);
	}

	public TabStyle getTabStyle() {
		return style;
	}

	public void setTextSize(float size) {
		this.size = size;
		setup();
	}
	
	public float getTextSize() {
		return size;
	}
	
	public void setTabColor(int tabIndex, int color) {
		 if (tabIndex < getTabCount() && tabIndex >= 0) {
			 if (contains(tabIndex))
				 colors.get(tabIndex).color = color;
			 else
				 colors.add(new TabColor(tabIndex, color, getTab(tabIndex).getLink()));
			 setup();
		 }
	}
	
	public void setFont(@NonNull Font font) {
		layout.setFont(font);
	}

	public void setFontStyle(@NonNull Font.Style style) {
		fontStyle = style;
		setup();
	}
	
	public Font getFont() {
		return layout.getFont();
	}
	
	public Font.Style getFontStyle() {
		return layout.getFontStyle();
	}
	
	protected TabColor getColor(int index) {
		for (TabColor tab : colors)
			if (tab.index == index)
				return tab;
		return null;
	}
	
	protected boolean contains(int index) {
		return getColor(index) != null;
	}

	public void setTabColor(int color) {
		for (int index = 0; index < getTabCount(); index++)
			setTabColor(index, color);
	}
	
    @Override
	public Background getViewBackground() {
		return layout.getViewBackground();
	}


	public void setTabTexts(@NonNull CharSequence... texts) {
		if (texts.length > 0) {
			adapter = new IAdapter(listener, texts);
			setup();
		}
	}
	
	public Tab getTab(int position) {
		return adapter.setOnTabClickListener(listener).getTab(position);
	}
	
	public int getTabCount() {
		return adapter.getCount();
	}
	
	public Adapter getAdapter() {
		return adapter;
	}
	
	public void setTabDivider(@NonNull Divider divider) {
		adapter.setDivider(divider);
	}
	
	public Divider getTabDivider() {
		return adapter.getDivider();
	}

	public boolean isAllTab() {
		return style.equals(TabStyle.AllLink);
	}
	
	@Override
	public void setViewWidth(int width) {
		layout.setViewWidth(width);
	}

	@Override
	public void setViewHeight(int height) {
		layout.setViewHeight(height);
	}

	@Override
	public int getViewHeight() {
		return layout.getViewHeight();
	}

	@Override
	public int getViewWidth() {
		return layout.getViewWidth();
	}
	
    @Override
	public void setBackground(@NonNull Background bg) {
		layout.setBackground(bg);
	}
	
	
	
	
	public interface OnTabClickListener {
		void onTabClicked(TextTabView view, Tab tab, int position);
	}
	
	protected static class TabColor {
		public int index, color;
		public CharSequence text;

		public TabColor(int index, int color, CharSequence text) {
			this.index = index;
			this.color = color;
			this.text = text;
		}
	}
	
	public enum TabStyle {
		AllLink,
		MonoLink,
		NoLink,
		PartialLink
	}
	
	public interface Tab {
		int getPosition();
		CharSequence getLink();
		OnTabClickListener getOnTabClickListener();
	}
	
	protected static class ITab implements Tab {
		protected int pos;
		protected CharSequence text;
		protected OnTabClickListener listener;
		
		public ITab(int pos, CharSequence text, OnTabClickListener l) {
			this.pos = pos;
			this.text = text;
			setOnTabClickListener(l);
		}

		@Override
		public int getPosition() {
			return pos;
		}

		@Override
		public CharSequence getLink() {
			return text;
		}

		@Override
		public OnTabClickListener getOnTabClickListener() {
			return listener;
		}
		
		public void setText(CharSequence text) {
			this.text = text;
		}
		
		public void setOnTabClickListener(OnTabClickListener listener) {
			this.listener = listener;
		}
	}
	
	public static abstract class Adapter {
		protected List<CharSequence> tabs;
		protected OnTabClickListener listener;
		protected TextTabView view;
		protected char divider;
		protected Divider eDivider;
		
		public Adapter(OnTabClickListener l, List<CharSequence> tabs) {
			this.tabs = tabs;
			listener = l;
			setDivider(Divider.Greater);
		}
		
		public void setDivider(Divider d) {
			if (d == null)
				return;
			eDivider = d;
			switch (d) {
				case Dash:
					divider = '-';
					break;
				case Dot:
					divider = '.';
					break;
				case Greater:
					divider = '>';
					break;
				case Slash:
					divider = '/';
			}
		}
		
		public Divider getDivider() {
			return eDivider;
		}
		
		abstract Tab getTab(int position);
		
		public TextTabView getView() {
			return view;
		}

		public int getCount() {
			return tabs.size();
		}

		public boolean isEmpty() {
			return tabs.isEmpty();
		}
		
		
		
		
		public interface OnDataChangedListener {
			void onDataChanged(Adapter adapter);
		}
	}

	public enum Divider {
		Dash,
		Dot,
		Greater,
		Slash
	}
	
	protected static class IAdapter extends Adapter {
		public OnDataChangedListener dListener;
		
		public IAdapter(OnTabClickListener l, CharSequence... tabs) {
			this(l, Arrays.asList(tabs));
		}

		public IAdapter(OnTabClickListener l, List<CharSequence> tabs) {
			super(l, tabs);
		}
		
		public Tab getTab(int position) {
			return position >= 0 && position < getCount() ? new ITab(position, tabs.get(position), listener) : null;
		}
		
		public void setView(@NonNull TextTabView view) {
			this.view = view;
		}

		public String getText() {
			StringBuilder text = new StringBuilder();
			for (int pos = 0; pos < getCount(); pos++)
				text.append(getTab(pos).getLink()).append(" ").append(divider).append(" ");
			return text.toString();
		}
		
		public IAdapter setOnTabClickListener(OnTabClickListener l) {
			listener = l;
			return this;
		}
		
		public void setOnDataChangedListener(@NonNull OnDataChangedListener l) {
			dListener = l;
		}
		
		public CharSequence[] getLinks() {
			CharSequence[] links = new CharSequence[tabs.size()];
			int index = 0;
			for (CharSequence link : tabs) {
				links[index] = link;
				index++;
			}
			return links;
		}
		
		
	}
	
}
