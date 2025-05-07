use parse::*;

mod parse;
mod program;
mod program_instruction;

#[allow(dead_code)]
pub fn part_1() {
    let mut program = parse(true);
    let (_, b) = program.run();

    println!("Part 1: {}", b);
}

#[allow(dead_code)]
pub fn part_2() {
    let mut program = parse(true);
    program.set_registers(1, 0);
    let (_, b) = program.run();

    println!("Part 2: {}", b);
}
