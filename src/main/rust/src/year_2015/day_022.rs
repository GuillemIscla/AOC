use parse::*;

mod game;
mod parse;
mod spell;
mod warrior;
mod wizard;

//1362 too high
#[allow(dead_code)]
pub fn part_1() {
    let game = parse(true);

    let mut mana_to_use = 0;
    let hard = false;
    let print = false;

    while !game.win_with_mana(mana_to_use, hard, print) {
        mana_to_use += 1;
    }

    println!("Part 1: {}", mana_to_use);
}

#[allow(dead_code)]
pub fn part_2() {
    let game = parse(true);

    let mut mana_to_use = 0;
    let hard = true;
    let print = false;

    while !game.win_with_mana(mana_to_use, hard, print) {
        mana_to_use += 1;
    }

    println!("Part 2: {}", mana_to_use);
}
