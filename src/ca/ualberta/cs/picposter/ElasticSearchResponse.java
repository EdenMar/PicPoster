package ca.ualberta.cs.picposter;


//original classes and credit belong to https://github.com/rayzhangcl/ESDemo/tree/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei

public class ElasticSearchResponse<T> {
    String _index;
    String _type;
    String _id;
    int _version;
    boolean exists;
    T _source;
    double max_score;
    public T getSource() {
        return _source;
    }
}
