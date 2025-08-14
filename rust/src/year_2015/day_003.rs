use crate::year_2015::day_003::position::Position;
use std::collections::HashSet;

use crate::year_2015::day_003::parse::parse;

mod direction;
mod parse;
mod position;

#[allow(dead_code)]
pub fn part_1() {
    let input = parse(true);
    let mut position_set = HashSet::new();
    let mut position = Position::new(0, 0);
    position_set.insert(position.clone());
    input.into_iter().for_each(|direction| {
        position_set.insert(position.clone());
        position = position.add(direction);
    });
    println!("Part 1: {}", position_set.len());
}

#[allow(dead_code)]
pub fn part_2() {
    let input = parse(true);
    let mut position_set = HashSet::new();
    let mut santa_position = Position::new(0, 0);
    let mut robo_santa_position = Position::new(0, 0);
    let mut santa_turn = true;
    position_set.insert(santa_position.clone());
    position_set.insert(robo_santa_position.clone());
    input.into_iter().for_each(|direction| {
        if santa_turn {
            santa_position = santa_position.add(direction);
            position_set.insert(santa_position.clone());
        } else {
            robo_santa_position = robo_santa_position.add(direction);
            position_set.insert(robo_santa_position.clone());
        }
        santa_turn = !santa_turn;
    });
    println!("Part 2: {}", position_set.len());
}
