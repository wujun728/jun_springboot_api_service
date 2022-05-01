try {
    try {
        return 1 / 0;
    } catch(e) {
        return 1;
    }
} catch(e) {
    return 3;
}
