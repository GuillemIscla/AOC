use crate::year_2015::day_014::reindeer::Reindeer;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> Vec<Reindeer> {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Vec<Reindeer>> {
    let file_path = "inputs/year_2015/day_014.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .into_iter()
            .map(|raw| Reindeer::from_raw(&raw))
            .collect::<io::Result<Vec<Reindeer>>>()
    } else {
        indoc! {"Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
        Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>()
        .into_iter()
        .map(|raw| Reindeer::from_raw(&raw))
        .collect::<io::Result<Vec<Reindeer>>>()
    }
}
