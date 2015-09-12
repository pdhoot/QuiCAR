var cars = require('../config/cars.json');

var brands_hash = {};
var models_hash = {};

load_brands_hash();
load_models_hash();

function load_brands_hash() {
  for (var index = 0; index < cars['Brand_name'].length; index++) {
    brands_hash[cars['Brand_name'][index]] = 1;
  }
}

function load_models_hash() {
  for (var index = 0; index < cars['Model'].length; index++) {
    models_hash[cars['Model'][index]] = 1;
  }
}

function get_ads(query_string) {
  if (query_string == null) {
    return {message : "null"};
  }
}

// Exporting module functions
exports.get_ads = get_ads;