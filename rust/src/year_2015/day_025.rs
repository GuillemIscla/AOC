use manual_utils::CodeInManual;
use parse::*;

mod manual_utils;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let (target_row, target_column) = parse(true);
    let mut code = CodeInManual::first_code();
    while !(code.row == target_row && code.column == target_column) {
        code = code.next()
    }
    println!("Part 1: {}", code.code);
}

#[allow(dead_code)]
pub fn part_2() {
    let input = "You got 50*!";
    println!("Part 2: {}", input);
}
