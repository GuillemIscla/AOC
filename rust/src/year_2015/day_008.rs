use parse::parse;
use string_utils::{escape_input, size_in_memory};

mod parse;
mod string_utils;

#[allow(dead_code)]
pub fn part_1() {
    let input = parse(true);
    let sizes: usize = input.iter().map(|s| s.len()).sum();
    let sizes_in_memory: usize = input.iter().map(|s| size_in_memory(s)).sum();
    println!("Part 1: {}", sizes - sizes_in_memory);
}

#[allow(dead_code)]
pub fn part_2() {
    let input = parse(true);
    let original_sizes: usize = input.iter().map(|s| s.len()).sum();
    let input_escaped = input.iter().map(escape_input).collect::<Vec<String>>();
    let sizes_in_memory: usize = input_escaped.iter().map(|s| s.len()).sum();
    println!("Part 2: {}", sizes_in_memory - original_sizes);
}
