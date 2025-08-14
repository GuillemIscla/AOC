use crate::year_2015::day_004::parse::parse;
use md5;

mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let input = parse(true);
    let mut n = 1;
    let mut new_input = input.clone() + &n.to_string();
    let mut digest = md5::compute(&new_input);
    let mut hash = format!("{:x}", digest);

    while &hash[0..5] != "00000" {
        n += 1;
        new_input = input.clone() + &n.to_string();
        digest = md5::compute(&new_input);
        hash = format!("{:x}", digest);
    }
    println!("Part 1: {}", n);
}

#[allow(dead_code)]
pub fn part_2() {
    let input = parse(true);
    let mut n = 1;
    let mut new_input = input.clone() + &n.to_string();
    let mut digest = md5::compute(&new_input);
    let mut hash = format!("{:x}", digest);

    while &hash[0..6] != "000000" {
        n += 1;
        new_input = input.clone() + &n.to_string();
        digest = md5::compute(&new_input);
        hash = format!("{:x}", digest);
    }
    println!("Part 2: {}", n);
}
