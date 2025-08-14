use crate::year_2015::vector_utils::*;
use parse::parse;

mod distance_notes;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let distance_notes = parse(true);
    let permutations = get_permutations(distance_notes.city_count());
    let min_route = permutations
        .iter()
        .map(|permutation| distance_notes.permute_and_travel(permutation.to_vec()))
        .min()
        .unwrap();
    println!("Part 1: {}", min_route);
}

#[allow(dead_code)]
pub fn part_2() {
    let distance_notes = parse(true);
    let permutations = get_permutations(distance_notes.city_count());
    let max_route = permutations
        .iter()
        .map(|permutation| distance_notes.permute_and_travel(permutation.to_vec()))
        .max()
        .unwrap();
    println!("Part 2: {}", max_route);
}
