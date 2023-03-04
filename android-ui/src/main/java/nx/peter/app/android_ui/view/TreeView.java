package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import nx.peter.app.android_ui.R;

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
        holder = new ArrowHolder(this); //, new IconItem(getContext(), R.drawable.github, "Main Text", "Description goes here..."));
        view.setDefaultAnimation(true);
        // view.setDefaultViewHolder(ArrowHolder.class);
        view.setDefaultAnimation(true);
        view.setRoot(new TreeNode(new IconItem(getContext(), R.drawable.github, "Main Text", "Description goes here...")));


    }

    public void setOnNodeClickListener(OnNodeClickListener listener) {
        holder.setItemClickListener(listener);
    }

    public void setOnNodeLongClickListener(OnNodeLongClickListener listener) {
        holder.setItemLongClickListener(listener);
    }

    public void setRoot(@NonNull CharSequence parent, @DrawableRes int icon, @NonNull CharSequence text) {

    }

    public void addNode(@NonNull CharSequence parent, @DrawableRes int icon, @NonNull CharSequence text, @NonNull CharSequence description) {

    }

    public void addNodes(@NonNull CharSequence parent, @DrawableRes int icon, @NonNull CharSequence... texts) {
        addNodes(parent, icon, "", texts);
    }

    public void addNodes(@NonNull CharSequence parent, @DrawableRes int icon, @NonNull CharSequence description, @NonNull CharSequence... texts) {

    }

    protected void setup() {

    }

    public Settings getSettings() {
        return null;
    }


    public interface Settings {

    }

    public interface OnNodeClickListener {
        void onClick(@NonNull Node node);
    }

    public interface OnNodeLongClickListener {
        boolean onLongClick(@NonNull Node node);
    }

    protected static class ISettings implements Settings {

    }

    public interface Node {
        TreeView getTreeView();
        boolean hasChild();
    }

    protected static class INode implements Node {
        TreeView view;
        TreeNode node;
        IconItem item;

        public INode(TreeView view, TreeNode node, IconItem item) {
            this.view = view;
            this.node = node;
            this.item = item;
        }

        @Override
        public TreeView getTreeView() {
            return view;
        }

        @Override
        public boolean hasChild() {
            return !node.getChildren().isEmpty();
        }
    }

    public interface NodeItem {
        Node getNode();
    }


    protected static class ArrowHolder extends TreeNode.BaseNodeViewHolder<IconItem> {
        TreeView treeView;
        protected OnNodeClickListener itemClickListener;
        protected OnNodeLongClickListener itemLongClickListener;

        public ArrowHolder(TreeView view) {
            super(view.getContext());
            this.treeView = view;
        }

        public void setItemClickListener(OnNodeClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setItemLongClickListener(OnNodeLongClickListener itemLongClickListener) {
            this.itemLongClickListener = itemLongClickListener;
        }

        public OnNodeClickListener getItemClickListener() {
            return itemClickListener;
        }

        public OnNodeLongClickListener getItemLongClickListener() {
            return itemLongClickListener;
        }

        @Override
        public View createNodeView(TreeNode node, IconItem value) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_arrow_tree, null, false);
            CheckBox arrow = view.findViewById(R.id.arrow);
            ImageView icon = view.findViewById(R.id.icon);
            StyledText text = view.findViewById(R.id.text);
            ScrollingTextView detail = view.findViewById(R.id.detail);

            icon.setImageDrawable(value.icon);
            text.setText(value.text);
            detail.setText(value.description);

            arrow.setOnCheckedChangeListener((button, checked) -> node.setExpanded(checked));

            /*
            node.setClickListener((node1, value1) -> {
                if (itemClickListener != null) itemClickListener.onClick(new INode(treeView, node1, (IconItem) value1));
            });

            node.setLongClickListener((node2, value2) -> itemLongClickListener != null && itemLongClickListener.onLongClick(new INode(treeView, node2, (IconItem) value2)));
             */
            return view;
        }
    }

    protected static class IconItem {
        public final Drawable icon;
        public final String text, description;

        public IconItem(Context context, int icon, String text, String description) {
            this.icon = ContextCompat.getDrawable(context, icon);
            this.description = description;
            this.text = text;
        }
    }

}
