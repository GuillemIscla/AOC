use bright_grid::BrightGrid;
use light_grid::LightGrid;

use crate::year_2015::day_006::parse::parse;

mod bright_grid;
mod instruction;
mod light_action;
mod light_grid;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let instructions = parse(true);
    let mut grid = LightGrid::new();
    instructions
        .into_iter()
        .for_each(|instruction| grid.process(instruction));
    println!("Part 1: {}", grid.on_count());
}

#[allow(dead_code)]
pub fn part_2() {
    let instructions = parse(true);
    let mut grid = BrightGrid::new();
    instructions
        .into_iter()
        .for_each(|instruction| grid.process(instruction));
    println!("Part 2: {}", grid.on_count());
}
