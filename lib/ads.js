var cars = require('../config/cars.json');
var ads = require('../config/ads.json')['ads'];

//-----------------------------------------------------------------------------
// Generating and loading hash tables from cars details
//-----------------------------------------------------------------------------
var brands_hash = {};
var models_hash = {};

initialize_brands_hash();
initialize_models_hash();
load_ads();

function initialize_brands_hash() {
  for (var index = 0; index < cars['Brand_name'].length; index++) {
    brands_hash[cars['Brand_name'][index]] = [];
  }
}

function initialize_models_hash() {
  for (var index = 0; index < cars['Model'].length; index++) {
    models_hash[cars['Model'][index]] = [];
  }
}

function load_ads() {
  for (var index = 0; index < ads.length; index++) {
    var brand_name = ads[index]['attribute_Brand_name'];
    var model = ads[index]['attribute_Model'];
    brands_hash[brand_name].push(ads[index]);
    models_hash[model].push(ads[index]);
  }
}
//-----------------------------------------------------------------------------

// Function to get all ads
function get_all_ads() {
  return {ads: ads};
}

// Function to get ads by brand_name
function get_ads_by_brand_name(brand_name) {
  return {ads: brands_hash[brand_name]};
}

// Function to get ads by model
function get_ads_by_model(model) {
  return {ads: models_hash[model]};
}

// Function to get ads from the query string
function get_ads(brand_name, model) {
  if (brand_name == null && model == null) {
    return get_all_ads();
  }
  if (model == null && brand_name != null) {
    return get_ads_by_brand_name(brand_name);
  }
  if (model != null) {
    return get_ads_by_model(model);
  }
}

// Exporting module functions
exports.get_ads = get_ads;
