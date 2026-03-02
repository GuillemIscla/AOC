use parse::*;

mod homework;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let cephalopod_reading = false;
    let homework = parse(None, cephalopod_reading);

    println!("Part 1: {}", homework.operate().into_iter().sum::<i64>())
}

#[allow(dead_code)]
pub fn part_2() {
    let cephalopod_reading = true;
    let homework = parse(None, cephalopod_reading);

    println!("Part 2: {}", homework.operate().into_iter().sum::<i64>())
}
