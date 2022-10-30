export function parseParam(param, key) {
  var paramStr = "";
  if (param instanceof String || param instanceof Number || param instanceof Boolean) {
    paramStr += "&" + key + "=" + encodeURIComponent(param);
  } else {
    _.each(param, function(i) {
      var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
      paramStr += '&' + parseParam(this, k);
    })
  }
  return paramStr.substr(1);
};
