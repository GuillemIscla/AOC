use crate::year_2015::day_007::parse::parse;

mod bitwise_gate;
mod bitwise_gate_input;
mod circuit;
mod circuit_part;
mod circuit_part_input;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let mut result = parse(true);
    println!("Part 1: {}", result.get_wire_value("a"));
}

#[allow(dead_code)]
pub fn part_2() {
    let mut result = parse(true);
    let first_iteration_a = result.get_wire_value("a");
    result.clear_cache();
    result.cache_wire_value("b", first_iteration_a);
    println!("Part 2: {}", result.get_wire_value("a"));
}
