function isEmpty(val){
    if (!val) {
        if (!((val === 0) || (val === false))) {
            return true;
        }
    }
    return false;
}