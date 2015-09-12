// Requireing external modules
var express = require('express');
var body_parser = require('body-parser');
var ads = require('./lib/ads.js');

// Requiring config
var config = require('./config/config.json');

var app = express();

// Configure app to use body-parser
app.use(body_parser.urlencoded({ extended: true }));
app.use(body_parser.json());

// Set up our router
var router = express.Router();

router.get('/getAds', function(req, res) {
  var query_string = req.query.query_string || null;
  var json_response = ads.get_ads(query_string);
  res.json(json_response);
});

// Register our router
app.use('/', router);

// Start the server
app.listen(config['port']);
console.log("Server started on port: " + config['port']);
