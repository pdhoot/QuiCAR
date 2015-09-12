package in.co.sdslabs.quickr;

import android.util.Log;

import com.quinny898.library.persistentsearch.SearchBox;

/**
 * Created by arpit on 12/9/15.
 */
public class SearchRequest {

    public SearchRequest(SearchBox search, SearchResponse.Listener<String> listener) {

        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                Log.d("search opened", "yeah");
            }


            @Override
            public void onSearchTermChanged() {
                //React to the search term changing
                //Called after it has updated results
                Log.d("search term changed", "yeah");

            }

            @Override
            public void onSearch(String searchTerm) {
                Log.d("onsearch", "yeah");

            }

            @Override
            public void onSearchClosed() {
                Log.d("search closed", "yeah");

                //Use this to un-tint the screen
            }

            @Override
            public void onSearchCleared() {
                Log.d("search cleared", "yeah");

            }
        });
    }
}
