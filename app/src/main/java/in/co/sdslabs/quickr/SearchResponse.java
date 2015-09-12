package in.co.sdslabs.quickr;

/**
 * Created by arpit on 12/9/15.
 */
public class SearchResponse<T> {

    public interface Listener<T> {

        public void onResponse(T response) ;
    }
}
