// Requiring external modules
var child_process = require('child_process');

//-----------------------------------------------------------------------------
// Generating and loading hash tables from cars details
//-----------------------------------------------------------------------------
var brands_hash = {};
var models_hash = {};
var all_ads = [];

generate_ads_json();
load_ads();

function generate_ads_json() {
  child_process.spawnSync('python', ['scripts/fetchAds.py']);
}

function load_ads() {
  var ads = require('../config/ads.json');
  for (var index = 0; index < ads.length; index++) {
    if(ads[index]['attribute_ad_type'] == 'offer') {
      var brand_name = ads[index]['attribute_brand_name'];
      var model = ads[index]['attribute_model'];

      if(brands_hash[brand_name] == undefined) {
        brands_hash[brand_name] = [];
      }
      if(models_hash[model] == undefined) {
        models_hash[model] = [];
      }

      brands_hash[brand_name].push(ads[index]);
      models_hash[model].push(ads[index]);
      all_ads.push(ads[index]);
    }
  }
}
//-----------------------------------------------------------------------------

// Function to get all ads
function get_all_ads() {
  return all_ads;
}

// Function to get ads by brand_name
function get_ads_by_brand_name(brand_name) {
  return brands_hash[brand_name.toLowerCase()];
}

// Function to get ads by model
function get_ads_by_model(model) {
  return models_hash[model.toLowerCase()];
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
