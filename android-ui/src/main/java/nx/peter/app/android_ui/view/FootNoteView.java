package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import nx.peter.app.android_ui.R;

import java.util.ArrayList;
import java.util.List;

public class FootNoteView extends AbstractView<FootNoteView> {
    protected List<SocialMedia> media;
    protected Background background;
    protected RecyclerView socials;
    protected SocialAdapter adapter;

    public FootNoteView(@NonNull Context c) {
        super(c);
    }

    public FootNoteView(@NonNull Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_foot_note, this);
        socials = findViewById(R.id.socials);
        reset();

    }

    protected void reset() {
        media = new ArrayList<>();
        setBackground(Background.Transparent);
        adapter = new SocialAdapter(media);

        socials.setAdapter(adapter);
        addAllSocials();
    }

    public void setSocials(Socials... socials) {
        media = new ArrayList<>();
        addSocials(socials);
    }

    public void removeAllSocials() {
        removeSocials(getSocials());
    }

    public void removeSocials(Socials... socials) {
        for (Socials s : socials) remove(s);
    }

    public void addSocials(Socials... socials) {
        for (Socials s : socials) add(s);
    }

    public void addAllSocials() {
        addSocials(getSocials());
    }

    protected Socials[] getSocials() {
        return new Socials[]{
                Socials.Facebook,
                Socials.Github,
                Socials.Instagram,
                Socials.Medium,
                Socials.Twitter,
                Socials.Whatsapp
        };
    }

    private void add(Socials s) {
        SocialMedia m = generate(s);
        if (!media.contains(m)) media.add(m);
        setup();
    }

    private SocialMedia generate(Socials s) {
        return new SocialMedia() {
            @Override
            public String getLink() {
                return FootNoteView.getLink(s);
            }

            @Override
            public int getLogo() {
                return FootNoteView.getLogo(s);
            }

            @Override
            public Socials getSocials() {
                return s;
            }

            @Override
            public boolean equals(@NonNull SocialMedia m) {
                return equals(m.getSocials());
            }

            @Override
            public boolean equals(@NonNull Socials socials) {
                return s.equals(socials);
            }
        };
    }

    private void remove(Socials s) {
        media.remove(generate(s));
        setup();
    }

    private static int getLogo(Socials socials) {
        switch (socials) {
            case Facebook:
                return R.drawable.facebook;
            case Github:
                return R.drawable.github;
            case Instagram:
                return R.drawable.instagram;
            case Medium:
                return R.drawable.medium;
            case Twitter:
                return R.drawable.twitter;
            case Whatsapp:
                return R.drawable.whatsapp;
            default:
                return R.drawable.ic_launcher_background;
        }
    }

    private static String getLink(Socials socials) {
        switch (socials) {
            case Facebook:
                return "https://facebook.com/";
            case Github:
                return "https://github.com/Petersburg2020";
            case Instagram:
                return "https://instagram.com/piservices_";
            case Medium:
                return "https://medium.com/Petersburgz";
            case Twitter:
                return "https://twitter.com/PIServices";
            case Whatsapp:
                return "wa.me/23480258140";
            default:
                return "";
        }
    }

    @Override
    public void setBackground(@NonNull Background b) {
        background = b;
    }

    @Override
    public Background getViewBackground() {
        return background;
    }


    protected void setup() {
        adapter.setMedia(media);
    }

    public enum Socials {
        Facebook,
        Github,
        Instagram,
        Medium,
        Twitter,
        Whatsapp
    }

    public interface SocialMedia {
        Socials getSocials();

        String getLink();

        int getLogo();

        boolean equals(@NonNull SocialMedia media);

        boolean equals(@NonNull Socials socials);
    }

    private static class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.SocialHolder> {
        List<SocialMedia> media;

        public SocialAdapter(List<SocialMedia> media) {
            setMedia(media);
        }

        public void setMedia(List<SocialMedia> media) {
            this.media = new ArrayList<>();
            addMedia(media);
        }

        @SuppressLint("NotifyDataSetChanged")
        public void addMedia(List<SocialMedia> media) {
            for (SocialMedia s : media)
                if (contains(s)) {
                    int index = indexOf(s);
                    this.media.remove(index);
                    this.media.add(index, s);
                } else this.media.add(s);
            notifyDataSetChanged();
        }

        @Nullable
        public SocialMedia get(@NonNull Socials socials) {
            for (SocialMedia s : media)
                if (s.getSocials().equals(socials))
                    return s;
            return null;
        }

        public int indexOf(@NonNull Socials socials) {
            int count = 0;
            for (SocialMedia s : media)
                if (s.getSocials().equals(socials))
                    return count;
                else count++;
            return -1;
        }

        public int indexOf(@NonNull SocialMedia media) {
            return indexOf(media.getSocials());
        }

        public boolean contains(@NonNull Socials socials) {
            return get(socials) != null;
        }

        public boolean contains(@NonNull SocialMedia media) {
            return contains(media.getSocials());
        }

        public SocialMedia getItem(int position) {
            return media.get(position);
        }

        public int getCount() {
            return media.size();
        }


        @NonNull
        @Override
        public SocialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SocialHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_socials, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SocialHolder holder, int position) {
            holder.setup(getItem(position));
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public static class SocialHolder extends RecyclerView.ViewHolder {
            final ImageTextButton view;

            public SocialHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView.findViewById(R.id.view);
            }

            public void setup(SocialMedia social) {
                view.setImageSize(60);

                view.setText(social.getLink());
                view.setImage(social.getLogo());
            }
        }
    }
}

