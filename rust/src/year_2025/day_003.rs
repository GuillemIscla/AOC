use parse::*;

mod battery_bank;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let battery_banks = parse(None);
    let result: u64 = battery_banks.iter().map(|bb| bb.max_joltage(2)).sum();

    println!("Part 1: {}", result)
}

#[allow(dead_code)]
pub fn part_2() {
    let battery_banks = parse(None);
    let result: u64 = battery_banks.iter().map(|bb| bb.max_joltage(12)).sum();

    println!("Part 2: {}", result);
}
