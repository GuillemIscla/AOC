use std::panic;

use parse::*;

mod parse;
mod present;
mod tree_area;

#[allow(dead_code)]
pub fn part_1() {
    let (presents, tree_areas) = parse(None);

    let mut fits = 0;
    let mut _does_not_fit = 0;
    let mut do_not_know = 0;

    tree_areas.iter().for_each(|tree_area| {
        if tree_area.it_surely_fits(&presents) {
            fits += 1;
        } else if tree_area.it_surely_does_not_fit(&presents) {
            _does_not_fit += 1;
        } else {
            do_not_know += 1;
        }
    });

    if do_not_know > 0 {
        panic!("Algorithm is too naive to solve the input");
    }

    println!("Part 1: {}", fits);
}

#[allow(dead_code)]
pub fn part_2() {
    println!("Part 2: 24*");
}
