function some(list, func) {
    if (!list) {
        return false;
    }
    var i, len = list.length;
    for (i = 0; i < len; ++i) {
        if (func(list[i]) === true) {
            return true;
        }
    }
    return false;
}