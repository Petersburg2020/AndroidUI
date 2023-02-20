package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import nx.peter.app.android_ui.R;

import java.util.Arrays;
import java.util.List;

public class TreeView extends AbstractView<TreeView> {
    protected AndroidTreeView view;
    protected LinearLayout layout;
    protected ArrowHolder holder;

    public TreeView(Context context) {
        super(context);
    }

    public TreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_tree, this);
        view = new AndroidTreeView(getContext());
        reset();

    }

    protected void reset() {
        holder = new ArrowHolder(getContext());
        view.setDefaultAnimation(true);

    }

    public void setRoot(@DrawableRes int icon, @NonNull CharSequence text) {

    }

    public void addNode(@DrawableRes int icon, @NonNull CharSequence text) {

    }

    public void addNodes(@NonNull CharSequence parent, @DrawableRes int icon, @NonNull CharSequence... texts) {

    }


    public Settings getSettings() {
        return null;
    }


    public interface Settings {

    }

    protected static class ISettings implements Settings {

    }

    public interface Node {
        TreeView getTreeView();
    }

    public interface NodeItem {
        Node getNode();
    }


    protected static class ArrowHolder extends TreeNode.BaseNodeViewHolder<IconItem> {
        protected List<IconItem> items;

        public ArrowHolder(Context context, IconItem... items) {
            this(context, Arrays.asList(items));
        }

        public ArrowHolder(Context context, List<IconItem> items) {
            super(context);
            this.items = items;
        }

        @Override
        public View createNodeView(TreeNode node, IconItem value) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_arrow_tree, null, false);
            CheckBox arrow = view.findViewById(R.id.arrow);
            ImageView icon = view.findViewById(R.id.icon);
            MultiActionText text = view.findViewById(R.id.text);
            ScrollingTextView detail = view.findViewById(R.id.detail);



            icon.setImageDrawable(value.icon);
            text.setText(value.text);

            arrow.setOnCheckedChangeListener((button, isChecked) -> node.setExpanded(isChecked));

            if (node.getChildren().isEmpty()) {

            }

            return view;
        }
    }

    protected static class IconItem {
        public final Drawable icon;
        public final String text;

        public IconItem(Context context, int icon, String text) {
            this.icon = ContextCompat.getDrawable(context, icon);
            this.text = text;
        }
    }

}
