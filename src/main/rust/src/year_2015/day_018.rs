use parse::*;

mod game_of_life_grid;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let mut game_of_life_grid = parse(true, false);
    let iterations = 100;
    for _ in 0..iterations {
        game_of_life_grid = game_of_life_grid.next();
    }

    println!("Part 1: {}", game_of_life_grid.lights_on());
}

#[allow(dead_code)]
pub fn part_2() {
    let mut game_of_life_grid = parse(true, true);
    let iterations = 100;
    for _ in 0..iterations {
        game_of_life_grid = game_of_life_grid.next();
    }

    println!("Part 2: {}", game_of_life_grid.lights_on());
}
