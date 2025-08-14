use std::fs::File;
use std::io::{self, BufRead, Error, ErrorKind};

pub fn parse() -> String {
    parse_internal().expect("Problems with parsing")
}

fn parse_internal() -> io::Result<String> {
    let file_path = "inputs/year_2015/day_001.txt";
    let file: File = File::open(file_path)?;
    let lines = io::BufReader::new(file)
        .lines()
        .collect::<io::Result<Vec<String>>>()?;
    let result = lines
        .first()
        .ok_or_else(|| Error::new(ErrorKind::InvalidData, "Error parsing the file"))?;

    Ok(result.to_string())
}
