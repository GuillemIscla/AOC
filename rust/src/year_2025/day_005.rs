use parse::*;

mod id_database;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let (id_database, ids) = parse(None);

    let fresh_ids = ids
        .into_iter()
        .filter(|&id| {
            id_database
                .ranges
                .iter()
                .any(|range| id >= range.0 && id <= range.1)
        })
        .count();

    println!("Part 1: {}", fresh_ids)
}

#[allow(dead_code)]
pub fn part_2() {
    let (id_database, _) = parse(None);

    let id_count: i64 = id_database
        .no_intersections()
        .ranges
        .into_iter()
        .map(|range| range.1 - range.0 + 1)
        .sum();

    println!("Part 2: {}", id_count)
}
