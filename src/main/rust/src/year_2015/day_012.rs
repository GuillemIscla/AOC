use account_utils::*;
use parse::parse;

mod account_utils;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let input = parse(None);
    let numbers = find_numbers(input);
    println!("Part 1: {}", numbers.iter().sum::<isize>());
}

#[allow(dead_code)]
pub fn part_2() {
    let input = parse(None);
    let numbers = find_numbers_exclude_red(input);
    println!("Part 2: {}", numbers.iter().sum::<isize>());
}
