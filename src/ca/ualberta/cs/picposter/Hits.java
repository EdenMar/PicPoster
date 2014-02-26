package ca.ualberta.cs.picposter;


//original classes and credit belong to https://github.com/rayzhangcl/ESDemo/tree/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei

import java.util.Collection;


public class Hits<T> {
    int total;
    double max_score;
    Collection<ElasticSearchResponse<T>> hits;
    public Collection<ElasticSearchResponse<T>> getHits() {
        return hits;
    }
    public String toString() {
        return (super.toString()+","+total+","+max_score+","+hits);
    }
}