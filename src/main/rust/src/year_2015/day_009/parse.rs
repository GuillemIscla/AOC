use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

use super::distance_notes::DistanceNotes;

pub fn parse(from_file: bool) -> DistanceNotes {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<DistanceNotes> {
    let file_path = "inputs/year_2015/day_009.txt";
    let file: File = File::open(file_path)?;

    let raw_input: Vec<String> = if from_file {
        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
    } else {
        indoc! {"London to Dublin = 464
                 London to Belfast = 518
                 Dublin to Belfast = 141"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect()
    };
    parse_internal(raw_input)
}

fn parse_internal(raw_input: Vec<String>) -> io::Result<DistanceNotes> {
    let raw_input_literal = raw_input.iter().map(|s| s.as_str()).collect();
    DistanceNotes::from_raw(raw_input_literal)
}
