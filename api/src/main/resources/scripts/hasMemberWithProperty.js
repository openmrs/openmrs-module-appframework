function hasMemberWithProperty(list, propName, val) {
    if (!list) {
        return false;
    }
    var i, len = list.length;
    for (i = 0; i < len; ++i) {
        if (list[i][propName] === val) {
            return true;
        }
    }
    return false;
}