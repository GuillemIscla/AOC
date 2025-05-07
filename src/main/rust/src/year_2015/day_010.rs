use look_and_say::*;
use parse::*;

mod look_and_say;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let mut line = parse(true);
    let iterations = 40;
    (0..iterations).for_each(|_| {
        line = next_line_iterative(&line);
    });
    println!("Part 1: {}", line.len());
}

#[allow(dead_code)]
pub fn part_2() {
    let mut line = parse(true);
    let iterations = 50;
    (0..iterations).for_each(|_| {
        line = next_line_iterative(&line);
    });
    println!("Part 2: {}", line.len());
}
