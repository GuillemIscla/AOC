use arithmetic_elves_utils::*;
use parse::*;

mod arithmetic_elves_utils;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let min_to_reach = parse(true);
    let mut house_number = 12;
    //All interesting numbers have small primes as divisors. We aren't skipping any strong
    //candidates if we skip candidates with big primes as divisors and that makes the operation faster.
    while min_to_reach > sum_divisors(house_number, Some(101), None) * 10 {
        //For the same token, we aren't skipping any strong candidates if we just iterate over
        //divsors of 12
        house_number += 12;
    }

    println!("Part 1: {}", house_number);
}

#[allow(dead_code)]
pub fn part_2() {
    let min_to_reach = parse(true);
    let mut house_number = 12;
    //On top of the strategies on part_1:
    //x We multiply by 11
    //x Diregard the divisors d which house_number / 50 <= d
    while min_to_reach > sum_divisors(house_number, Some(101), Some(50)) * 11 {
        house_number += 12;
    }

    println!("Part 2: {}", house_number);
}
