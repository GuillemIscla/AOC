use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> Vec<String> {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Vec<String>> {
    let file_path = if from_file {
        "inputs/year_2015/day_008.txt"
    } else {
        "inputs/year_2015/day_008_sample.txt"
    };
    let file: File = File::open(file_path)?;

    io::BufReader::new(file)
        .lines()
        .map(|r| r.map(|s| String::from(s.trim())))
        .collect::<io::Result<Vec<String>>>()
}
