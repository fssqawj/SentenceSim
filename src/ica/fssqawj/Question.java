package ica.fssqawj;


/**
 * Created by fssqa on 2015/12/8.
 */
public class Question implements Comparable<Object> {
    String content = null;

    double src;

    public String getContent() {
        return content;

    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getSrc() {
        return src;
    }

    public void setSrc(double src) {
        this.src = src;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        Question q = (Question)o;
        if(this.getSrc() > q.getSrc())return -1;
        else if(this.getSrc() == q.getSrc())return 0;
        return 1;
    }
}
