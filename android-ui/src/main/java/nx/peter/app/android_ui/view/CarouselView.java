package nx.peter.app.android_ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.mig35.carousellayoutmanager.CarouselLayoutManager;
import com.mig35.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.mig35.carousellayoutmanager.CenterScrollListener;
import nx.peter.app.android_ui.R;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CarouselView extends AView<CarouselView> {
    protected RecyclerView view;

    public CarouselView(@NonNull Context context) {
        super(context);
    }

    public CarouselView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_carousel, this);
        view = findViewById(R.id.view);
        reset();

    }

    @Override
    protected void reset() {
        CarouselLayoutManager manager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        manager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        view.setLayoutManager(manager);
        view.setHasFixedSize(true);
        view.addOnScrollListener(new CenterScrollListener());


        setAdapter((Adapter<?, ? extends RecyclerView.ViewHolder>) null);

    }

    public <Model, ViewHolder extends RecyclerView.ViewHolder> void setAdapter(@Nullable Adapter<Model, ViewHolder> adapter) {
        view.setAdapter(adapter);
    }

    public <Model, ViewHolder extends RecyclerView.ViewHolder> Adapter<Model, ViewHolder> getAdapter() {
        return (Adapter<Model, ViewHolder>) view.getAdapter();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        view.setPadding(left, top, right, bottom);
    }


    public static abstract class Adapter<Model, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
        final List<Model> models;


        protected Adapter(List<Model> models) {
            this.models = models;
        }

        public Model getItem(int position) {
            return models.get(position);
        }

        public void addItem(@NonNull Model model) {
            models.add(model);
            notifyItemInserted(getItemCount() - 1);
        }

        public void removeItem(int position) {
            if (position >= 0 && position < getItemCount()) {
                models.remove(position);
                notifyItemRemoved(position);
            }
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

    }


    static class DefaultAdapter extends Adapter<DefaultModel, DefaultAdapter.ViewHolder> {

        protected DefaultAdapter(DefaultModel... defaultModels) {
            this(Arrays.asList(defaultModels));
        }

        protected DefaultAdapter(List<DefaultModel> defaultModels) {
            super(defaultModels);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    static class DefaultModel {
        public int image;
        public String text;

        public DefaultModel(int image, String text) {
            this.image = image;
            this.text = text;
        }
    }

}
