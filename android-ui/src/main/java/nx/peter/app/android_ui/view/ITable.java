package nx.peter.app.android_ui.view;

import android.view.View;
import androidx.annotation.NonNull;

public interface ITable<T extends View> extends IView<T> {
    int getColumnCount();

    void setRowCellsData(@NonNull Object... data);

    void setColumnHeadings(@NonNull CharSequence... headings);

    void setRowHeadings(@NonNull CharSequence... headings);

    void setSizeUnit(SizeUnit unit);

    void setColumnWidth(int column, int width);

    enum SizeUnit {
        Dp,
        Px
    }

    interface Cell<V extends View> extends StyledView<V> {
        int getColumn();

        int getRow();

        ITable<?> getTable();
    }

    interface OnCellClickedListener {
        void onClick();
    }

}
