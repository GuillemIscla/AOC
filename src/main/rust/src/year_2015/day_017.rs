use eggnog_utils::*;
use parse::*;

mod eggnog_utils;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let boxes = parse(true);
    let eggnog = 150;
    let result = store_eggnog_count(&boxes, eggnog);
    println!("Part 1: {}", result);
}

#[allow(dead_code)]
pub fn part_2() {
    let boxes = parse(true);
    let eggnog = 150;
    let (result, _) = store_eggnog_count_min_boxes(&boxes, eggnog);
    println!("Part 2: {:?}", result);
}
