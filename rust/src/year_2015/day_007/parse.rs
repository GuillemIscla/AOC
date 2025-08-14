use crate::year_2015::day_007::circuit::Circuit;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> Circuit {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Circuit> {
    let file_path = "inputs/year_2015/day_007.txt";
    let file: File = File::open(file_path)?;

    let raw_input: Vec<String> = if from_file {
        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
    } else {
        indoc! {"
            123 -> x
            456 -> y
            x AND y -> d
            x OR y -> e
            x LSHIFT 2 -> f 
            y RSHIFT 2 -> g
            NOT x -> h
            NOT y -> i"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect()
    };
    parse_internal(raw_input)
}

fn parse_internal(raw_input: Vec<String>) -> io::Result<Circuit> {
    let raw_input_literal = raw_input.iter().map(|s| s.as_str()).collect();
    Circuit::from_raw(raw_input_literal)
}
