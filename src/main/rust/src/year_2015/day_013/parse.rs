use crate::year_2015::day_013::arrangement_notes::ArrangementNotes;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> ArrangementNotes {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<ArrangementNotes> {
    let file_path = "inputs/year_2015/day_013.txt";
    let file: File = File::open(file_path)?;

    let raw_input: Vec<String> = if from_file {
        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
    } else {
        indoc! {"Alice would gain 54 happiness units by sitting next to Bob.
                 Alice would lose 79 happiness units by sitting next to Carol.
                 Alice would lose 2 happiness units by sitting next to David.
                 Bob would gain 83 happiness units by sitting next to Alice.
                 Bob would lose 7 happiness units by sitting next to Carol.
                 Bob would lose 63 happiness units by sitting next to David.
                 Carol would lose 62 happiness units by sitting next to Alice.
                 Carol would gain 60 happiness units by sitting next to Bob.
                 Carol would gain 55 happiness units by sitting next to David.
                 David would gain 46 happiness units by sitting next to Alice.
                 David would lose 7 happiness units by sitting next to Bob.
                 David would gain 41 happiness units by sitting next to Carol."}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect()
    };
    parse_internal(raw_input)
}

fn parse_internal(raw_input: Vec<String>) -> io::Result<ArrangementNotes> {
    let raw_input_literal = raw_input.iter().map(|s| s.as_str()).collect();
    ArrangementNotes::from_raw(raw_input_literal)
}
