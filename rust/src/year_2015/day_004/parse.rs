use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> String {
    parse_internal(from_file).expect("Error on parsing")
}

pub fn parse_internal(from_file: bool) -> io::Result<String> {
    if from_file {
        let file_path = "inputs/year_2015/day_004.txt";
        let file: File = File::open(file_path)?;
        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()
            .map(|v| v[0].to_string())
    } else {
        Ok("abcdef".to_string())
    }
}
