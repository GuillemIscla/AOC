use parse::*;

mod game;
mod item;
mod parse;
mod warrior;

#[allow(dead_code)]
pub fn part_1() {
    let mut game = parse(true);

    let mut gold = game.min_weapon_cost();
    let aim_to_win = true;
    let print = false;

    while !game.win_lose_one_game_with_gold(gold, aim_to_win, print) {
        gold += 1;
    }

    println!("Part 1: {}", gold);
}

//126 too low
//127 too low
#[allow(dead_code)]
pub fn part_2() {
    let mut game = parse(true);

    let mut gold = game.max_you_can_spend();
    let aim_to_win = false;
    let print = false;

    while !game.win_lose_one_game_with_gold(gold, aim_to_win, print) {
        gold -= 1;
    }

    println!("Part 2: {}", gold);
}
