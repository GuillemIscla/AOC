use crate::year_2025::day_009::floor::Floor;
use parse::*;
use std::cmp::max;

mod floor;
mod parse;
mod tile;
mod tile_color;

#[allow(dead_code)]
pub fn part_1() {
    let tiles = parse(None);

    let mut max_area = 0;
    for tile_i in &tiles {
        for tile_j in &tiles {
            let area = tile_i.rectangle(tile_j);
            max_area = max(max_area, area);
        }
    }

    println!("Part 1: {}", max_area);
}

// 4582310446 too high
// 1513792010
#[allow(dead_code)]
pub fn part_2() {
    let tiles = parse(None);

    let floor = Floor::new(&tiles);

    let mut max_area = 0;
    (0..tiles.len()).for_each(|i| {
        let tile_i = &tiles[i];
        (i..tiles.len()).for_each(|j| {
            let tile_j = &tiles[j];
            let area = tile_i.rectangle(tile_j);
            if area > max_area && floor.rectangle_valid(tile_i, tile_j).unwrap() {
                max_area = area;
            }
        });
    });

    println!("Part 2: {}", max_area);
}
