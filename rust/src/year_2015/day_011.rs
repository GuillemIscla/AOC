use parse::parse;
use password_utils::*;

mod parse;
mod password_utils;

#[allow(dead_code)]
pub fn part_1() {
    let mut password = parse(None);
    password = next_password(password);
    while !security_1(&password) || !security_2(&password) || !security_3(&password) {
        password = next_password(password);
    }
    println!("Part 1: {}", password);
}

#[allow(dead_code)]
pub fn part_2() {
    let mut password = parse(None);
    password = next_password(password);
    while !security_1(&password) || !security_2(&password) || !security_3(&password) {
        password = next_password(password);
    }
    password = next_password(password);
    while !security_1(&password) || !security_2(&password) || !security_3(&password) {
        password = next_password(password);
    }
    println!("Part 2: {}", password);
}
