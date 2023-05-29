package nx.peter.app.android_ui.view.util;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import nx.peter.java.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Regex {
    String source;

    Regex(CharSequence source) {
        setSource(source);
    }

    public static Regex getInstance(CharSequence source) {
        return new Regex(source);
    }

    public void setSource(CharSequence source) {
        this.source = source != null ? source.toString() : "";
    }

    public CharSequence getSource() {
        return source;
    }

    public Properties split(Object delimiter) {
        List<Property> properties = new ArrayList<>();

        int start = 0;

        Properties props = extract(delimiter);

        for (Property prop : props) {
            if (prop.getStart() > start) {
                if (prop.getStart() >= source.length() - 1) {
                    properties.add(new IProperty(source, source.substring(start), start, source.length() - 1));
                    break;
                }
                properties.add(new IProperty(source, source.substring(start, prop.getStart()), start, prop.getStart()));
            }
            start = prop.getEnd() + 1;
        }

        return new IProperties(source, properties);
    }

    public Properties extract(Object what) {
        List<Property> properties = new ArrayList<>();

        // Check for nullity
        if (what != null) {
            Pattern p = Pattern.compile(String.valueOf(what));
            Matcher m = p.matcher(source);

            // Perform task
            while (m.find()) properties.add(new IProperty(source, what, m.start(), m.end()));
        }

        return new IProperties(source, properties);
    }

    public Properties extractExact(Object what) {
        List<Property> properties = new ArrayList<>();

        // Check for nullity
        if (what != null) {
            Pattern p = Pattern.compile("\\b" + what + "\\b");
            Matcher m = p.matcher(source);

            // Perform task
            while (m.find()) properties.add(new IProperty(source, what, m.start(), m.end()));
        }

        return new IProperties(source, properties);
    }

    public int countExact(Object what) {
        return extractExact(what).size();
    }

    public int count(Object what) {
        return extract(what).size();
    }

    public CharSequence replaceAll(Object replace, Object what) {
        if (what == null || replace == null) return source;
        Pattern p = Pattern.compile(replace.toString());
        Matcher m = p.matcher(source);
        return m.replaceAll(what.toString());
    }

    public static boolean isEmail(CharSequence email) {
        Pattern p = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-zA-Z)]+\\.[(a-zA-Z)]{2,3}$");
        Matcher m = p.matcher(email);
        return m.matches();
    }


    public interface Property {
        int length();

        int getEnd();

        int getStart();

        Object getData();

        CharSequence getSource();

        boolean endsAt(int index);

        boolean beginsAt(int index);

        boolean equals(Object data);

        String toString(boolean format);

        String toString(boolean format, int indent);

        boolean equals(Property property);
    }

    public interface Properties extends Iterable<Property> {
        int size();

        boolean isEmpty();

        boolean isNotEmpty();

        Property get(int index);

        String toString(boolean format);

        String toString(boolean format, int indent);

        Properties getProperties(Object data);

        Property getPropertyAt(int index);

        boolean contains(Object data);

        boolean contains(Property property);

        boolean equals(Properties properties);
    }


    static class IProperty implements Property {
        CharSequence source;
        Object data;
        int start, end;

        public IProperty(CharSequence source, Object data, int start, int end) {
            this.source = source;
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        public int length() {
            return data.toString().length();
        }

        @Override
        public int getEnd() {
            return end;
        }

        @Override
        public int getStart() {
            return start;
        }

        @Override
        public Object getData() {
            return data;
        }

        @Override
        public CharSequence getSource() {
            return source;
        }

        @Override
        public boolean endsAt(int index) {
            return index == end;
        }

        @Override
        public boolean beginsAt(int index) {
            return start == index;
        }

        @Override
        public boolean equals(Object data) {
            return this.data.getClass().equals(data.getClass()) && this.data.equals(data);
        }

        @Override
        public String toString(boolean format) {
            return toString(format, 0);
        }

        @Override
        public String toString(boolean format, int indent) {
            return (format ? Util.tab(indent) : "") + "{" + (format ? "\n" + Util.tab(indent + 1) : "") + "\"data\": " + (data instanceof String ? "\"" + data + "\"" : data) + (format ? ",\n" + Util.tab(indent + 1) : ", ") + "\"start\": " + start + (format ? ",\n" + Util.tab(indent + 1) : ", ") + "\"end\": " + end + (format ? "\n" + Util.tab(indent) : "") + "}";
        }

        @Override
        public boolean equals(Property property) {
            return property != null && property.equals(data) && property.beginsAt(start) && property.endsAt(end);
        }

        @NonNull
        @Override
        public String toString() {
            return toString(false);
        }
    }

    static class IProperties implements Properties {
        List<Property> properties;
        String source;

        public IProperties(String source, List<Property> properties) {
            this.source = source;
            this.properties = properties;
        }

        @Override
        public int size() {
            return properties.size();
        }

        @Override
        public boolean isEmpty() {
            return properties.isEmpty();
        }

        @Override
        public boolean isNotEmpty() {
            return !isEmpty();
        }

        @Override
        public Property get(int index) {
            return index >= 0 && index < size() ? properties.get(index) : null;
        }

        @Override
        public String toString(boolean format) {
            return toString(format, 0);
        }

        @Override
        public String toString(boolean format, int indent) {
            count = 0;
            return (format ? "\n" + Util.tab(indent) : "") + "[" + properties.stream().map(property -> {
                count++;
                return (format ? "\n" + Util.tab(indent) : "") + property.toString(format, indent + 1) + (count < size() ? ", " : "");
            }).collect(Collectors.joining()) + (format ? "\n" + Util.tab(indent) : "") + "]";
        }

        @Override
        public Properties getProperties(Object data) {
            List<Property> props = new ArrayList<>();
            if (data != null && source.contains(data.toString()))
                for (Property p : properties)
                    if (p.equals(data)) props.add(p);
            return new IProperties(source, props);
        }

        @Override
        public Property getPropertyAt(int index) {
            if (index > -1 && index < source.length())
                for (Property p : properties)
                    if (index >= p.getStart() && index <= p.getEnd()) return p;
            return null;
        }

        @Override
        public boolean contains(Object data) {
            return getProperties(data).isNotEmpty();
        }

        @Override
        public boolean contains(Property property) {
            return property != null && properties.contains(property);
        }

        @Override
        public boolean equals(Properties properties) {
            return properties != null && this.properties.equals(((IProperties) properties).properties);
        }

        @NonNull
        @Override
        public Iterator<Property> iterator() {
            return properties.iterator();
        }

        int count;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @NonNull
        @Override
        public String toString() {
            return toString(false);
        }


    }

}
