package crawler;

import java.util.Objects;

public class Link {
    String title;
    String url;

    public Link(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.title, this.url);
    }

    @Override
    public boolean equals(Object object){
        if(object == null || !(object instanceof Link)) return false;

        Link otherLink = (Link) object;
        return otherLink.getTitle() == this.getTitle() && otherLink.getUrl() == this.getUrl();
    }

    @Override
    public String toString(){
        return String.format("%s : %s", title, url);
    }
}
