use std::fs::File;
use std::io::{self, BufRead, Error, ErrorKind};

pub fn parse(from_file: Option<usize>) -> String {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

pub fn switch_sample_and_parse(from_file: Option<usize>) -> io::Result<String> {
    let file_path = "inputs/year_2015/day_011.txt";
    let file: File = File::open(file_path)?;

    match from_file {
        None => io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .first()
            .ok_or_else(|| Error::new(ErrorKind::NotFound, "Value was not present"))
            .cloned(),
        Some(1) => Ok(String::from("abcdefgh")),

        _ => Ok(String::from("ghijklmn")),
    }
}
