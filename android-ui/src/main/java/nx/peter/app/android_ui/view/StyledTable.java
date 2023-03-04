package nx.peter.app.android_ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.listener.ITableViewListener;
import nx.peter.app.android_ui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StyledTable extends AbstractView<StyledTable> implements ITable<StyledTable> {
    protected TableView table;
    protected TableAdapter adapter;
    protected int columnCount;
    protected List<String> rowHeadings, columnHeadings;
    protected List<List<Object>> cells;

    public StyledTable(Context context) {
        super(context);
    }

    public StyledTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_styled_table, this);
        table = findViewById(R.id.table);
        reset();

    }

    protected void reset() {
        columnCount = 4;
        columnHeadings = new ArrayList<>();
        rowHeadings = new ArrayList<>();
        cells = new ArrayList<>();
        adapter = new TableAdapter(columnHeadings, rowHeadings, cells);
        table.setAdapter(adapter);
    }

    @Override
    public int getColumnCount() {
        return adapter.getColumnCount();
    }

    @Override
    public void setRowCellsData(@NonNull Object... data) {

    }

    @Override
    public void setColumnHeadings(@NonNull CharSequence... headings) {
        adapter.setColumnHeaderItems(Arrays.asList((String[]) headings));
    }

    @Override
    public void setRowHeadings(@NonNull CharSequence... headings) {
        adapter.setRowHeaderItems(Arrays.asList((String[]) headings));
    }


    @Override
    public void setSizeUnit(SizeUnit unit) {

    }

    @Override
    public void setColumnWidth(int column, int width) {
        table.setColumnWidth(column, width);
        table.setTableViewListener(new ITableViewListener() {
            @Override
            public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

            }

            @Override
            public void onCellDoubleClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

            }

            @Override
            public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cell, int column, int row) {

            }

            @Override
            public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeader, int column) {

            }

            @Override
            public void onColumnHeaderDoubleClicked(@NonNull RecyclerView.ViewHolder columnHeader, int column) {

            }

            @Override
            public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeader, int column) {

            }

            @Override
            public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeader, int row) {

            }

            @Override
            public void onRowHeaderDoubleClicked(@NonNull RecyclerView.ViewHolder rowHeader, int row) {

            }

            @Override
            public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeader, int row) {

            }
        });
    }

    protected void setup() {

    }

    protected static class TableAdapter extends AbstractTableAdapter<String, String, Object> {
        public TableAdapter(List<String> columns, List<String> rows) {
            this(columns, rows, new ArrayList<>());
        }

        public TableAdapter(@NonNull List<String> columns, @NonNull List<String> rows, @NonNull List<List<Object>> cells) {
            setColumnHeaderItems(columns);
            setRowHeaderItems(rows);
            setCellItems(cells);
        }

        public void addRowCells(int row, List<Object> cells) {
            mCellItems.add(row - 1, cells);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.table_cell, parent, false));
        }

        @Override
        public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable Object model, int column, int row) {
            assert model != null;
            ((ViewHolder) holder).setup(model.toString());
        }

        @NonNull
        @Override
        public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.table_cell, parent, false));
        }

        @Override
        public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable String model, int column) {
            assert model != null;
            ((ViewHolder) holder).setup(model);
        }

        @NonNull
        @Override
        public ViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.table_cell, parent, false));
        }

        @Override
        public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable String model, int row) {
            assert model != null;
            ((ViewHolder) holder).setup(model);
        }

        @NonNull
        @Override
        public View onCreateCornerView(@NonNull ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.table_cell, parent, false);
        }

        public int getColumnCount() {
            return mColumnHeaderItems.size();
        }

        public int getRowCount() {
            return mRowHeaderItems.size();
        }

        public static class ViewHolder extends AbstractViewHolder {
            StyledText cell;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cell = itemView.findViewById(R.id.cell);
            }

            public void setup(String data) {
                cell.setText(data);
            }
        }

    }

    public interface Item {

    }

    protected static class Header implements Item {
        public String data;

        public Header(String data) {
            this.data = data;
        }
    }

    protected static class CellItem implements Item {
        public Object data;

        public CellItem(Object data) {
            this.data = data;
        }
    }


}
