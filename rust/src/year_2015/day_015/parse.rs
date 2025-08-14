use crate::year_2015::day_015::ingredient::Ingredient;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> Vec<Ingredient> {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Vec<Ingredient>> {
    let file_path = "inputs/year_2015/day_015.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .into_iter()
            .map(|raw| Ingredient::from_raw(&raw))
            .collect::<io::Result<Vec<Ingredient>>>()
    } else {
        indoc! {"Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
        Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>()
        .into_iter()
        .map(|raw| Ingredient::from_raw(&raw))
        .collect::<io::Result<Vec<Ingredient>>>()
    }
}
