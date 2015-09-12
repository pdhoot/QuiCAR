function get_ads(query_string) {
  if (query_string == null) {
    return get_all_ads();
  }
}

// Exporting module functions
exports.get_ads = get_ads;