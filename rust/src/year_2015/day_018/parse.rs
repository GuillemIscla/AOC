use super::game_of_life_grid::GameOrLifeGrid;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool, corners_stuck: bool) -> GameOrLifeGrid {
    switch_sample_and_parse(from_file, corners_stuck).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool, corners_stuck: bool) -> io::Result<GameOrLifeGrid> {
    let file_path = "inputs/year_2015/day_018.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        let raw = io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .into_iter()
            .map(|raw| raw.chars().map(|c| c == '#').collect::<Vec<bool>>())
            .collect::<Vec<Vec<bool>>>();

        Ok(GameOrLifeGrid::new(raw, corners_stuck))
    } else {
        let raw = indoc! {".#.#.#
        ...##.
        #....#
        ..#...
        #.#..#
        ####.."}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>()
        .into_iter()
        .map(|raw| raw.chars().map(|c| c == '#').collect::<Vec<bool>>())
        .collect::<Vec<Vec<bool>>>();
        Ok(GameOrLifeGrid::new(raw, corners_stuck))
    }
}
