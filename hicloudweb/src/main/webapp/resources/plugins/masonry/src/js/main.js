'use strict';

/* global: Masonry */

var $ = require('jquery');
var Hanlebars = require('handlebars');

$(function() {
  var $c = $('#js-container');
  var tpl = $('#tpl-list').html();
  var compiler = Hanlebars.compile(tpl);
  var params = {
    loc: 'beijing',
    type: 'music',
    callback: '?',
    count: 10,
    start: 0
  };

  var getURL = function() {
    var queryStr = $.param(params);
    return 'https://api.douban.com/v2/event/list?' +
      decodeURIComponent(queryStr);
  };

  var getList = function(url) {
    $.getJSON(url).then(function(data) {
      params.start = params.start + 10;
      var $html = $(compiler(data));
      $c.append($html).masonry( 'appended', $html).masonry('layout');
    });
  };

  $c.masonry({
    itemSelector: '.msry-item'
  });

  getList(getURL());

  $('#load-more').on('click', function() {
    getList(getURL());
  });
});
