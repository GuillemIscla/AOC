use crate::year_2015::day_003::direction::Direction;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> Vec<Direction> {
    parse_internal(from_file).expect("Error on parsing")
}

// Now implement conversion from std::io::Error to your type
pub fn parse_internal(from_file: bool) -> io::Result<Vec<Direction>> {
    if from_file {
        let file_path = "inputs/year_2015/day_003.txt";
        let file: File = File::open(file_path)?;

        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .join("")
            .chars()
            .map(Direction::from_char)
            .collect()
    } else {
        let hardcoded = "^v^v^v^v^v".to_string();
        hardcoded.chars().map(Direction::from_char).collect()
    }
}
