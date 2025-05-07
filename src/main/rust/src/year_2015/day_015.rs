use ingredients_utils::best_mix_ingredients;
use parse::*;

mod ingredient;
mod ingredients_utils;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let ingredients = parse(true);
    let teaspoons = 100;
    let best_mix = best_mix_ingredients(teaspoons, &ingredients, None);
    println!("Part 1: {:?}", best_mix.score());
}

#[allow(dead_code)]
pub fn part_2() {
    let ingredients = parse(true);
    let teaspoons = 100;
    let best_mix = best_mix_ingredients(teaspoons, &ingredients, Some(500));
    println!("Part 2: {}", best_mix.score());
}
