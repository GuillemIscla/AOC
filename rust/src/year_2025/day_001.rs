use crate::year_2025::day_001::dial::Dial;
use parse::*;

mod dial;
mod parse;
mod rotation;

#[allow(dead_code)]
pub fn part_1() {
    let rotations = parse(None);
    let mut times_zero = 0;
    let mut dial = Dial::new();

    rotations.into_iter().for_each(|rotation| {
        dial.rotate(rotation);
        if dial.position == 0 {
            times_zero += 1;
        }
    });

    println!("Part 1: {}", times_zero);
}

#[allow(dead_code)]
pub fn part_2() {
    let rotations = parse(None);
    let mut times_clicked = 0;
    let mut dial = Dial::new();

    rotations.into_iter().for_each(|rotation| {
        times_clicked += dial.rotate(rotation);
    });

    println!("Part 2: {}", times_clicked);
}
