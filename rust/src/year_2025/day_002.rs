use parse::*;

mod parse;
mod pattern;
mod range;

#[allow(dead_code)]
pub fn part_1() {
    let ranges = parse(None);

    let invalid_ids_sum: i64 = ranges
        .into_iter()
        .map(|range| range.invalid_ids_sum(Some(2)) as i64)
        .sum();

    println!("Part 1: {}", invalid_ids_sum);
}

#[allow(dead_code)]
pub fn part_2() {
    let ranges = parse(None);

    let invalid_ids_sum: i64 = ranges
        .into_iter()
        .map(|range| range.invalid_ids_sum(None) as i64)
        .sum();

    println!("Part 2: {}", invalid_ids_sum);
}
