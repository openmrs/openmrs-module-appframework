function fullMonthsBetweenDates(earlierDate, laterDate) {
    var d1 = new Date(earlierDate);
    var d2 = new Date(laterDate);
    var monthsBetween = ((d2.getFullYear() - d1.getFullYear()) * 12) + (d2.getMonth() - d1.getMonth());
    if (d2.getDate() < d1.getDate()) {
        monthsBetween = monthsBetween - 1;
    }
    return monthsBetween;
}