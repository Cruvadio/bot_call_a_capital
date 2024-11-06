function chooseRandCapitalKey(keys) {
    var i = 0
    keys.forEach(function(elem) {
        i++
    })
    return  $jsapi.random(i);
}

function checkCapital(cities, key, city) {
    var response = false
    if (cities[key].value.name == city){
        response = true
    }
    
    return response;
}