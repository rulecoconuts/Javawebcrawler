package com.mikeandtheo.webcrawler.crawler.google.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.mikeandtheo.webcrawler.crawler.Link;

public class SimpleGooglePages implements GooglePages {
    String searchString;

    final List<GooglePage> pages = new ArrayList<>();

    public SimpleGooglePages() {

    }

    public SimpleGooglePages(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public boolean add(GooglePage e) {
        if (searchString != null)
            e.setSearchString(searchString);
        return pages.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends GooglePage> c) {
        return pages.addAll(c);
    }

    @Override
    public void clear() {
        pages.clear();
    }

    @Override
    public boolean contains(Object o) {
        return pages.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return pages.containsAll(c);
    }

    @Override
    public boolean isEmpty() {
        return pages.isEmpty();
    }

    @Override
    public Iterator<GooglePage> iterator() {
        return pages.iterator();
    }

    @Override
    public boolean remove(Object o) {
        return pages.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return pages.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return pages.retainAll(c);
    }

    @Override
    public int size() {
        return pages.size();
    }

    @Override
    public Object[] toArray() {
        return pages.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return pages.toArray(a);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (GooglePage googlePage : pages) {
            builder.append(googlePage.toString());
            builder.append("\n\n*\n\n");
        }
        return builder.toString();
    }

    @Override
    public Map<Integer, String> getPageNavLinks() {
        Map<Integer, String> pageNavLinks = new HashMap<>();
        for (GooglePage page : this.pages)
            pageNavLinks.putAll(page.getPageNavLinks());
        return pageNavLinks;
    }
}
