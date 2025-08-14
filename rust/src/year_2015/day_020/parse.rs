use std::fs::File;
use std::io::{self, BufRead, Error, ErrorKind};

pub fn parse(from_file: bool) -> u32 {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<u32> {
    let file_path = "inputs/year_2015/day_020.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        let mut lines = io::BufReader::new(file).lines();

        let number = lines
            .next()
            .ok_or("File is empty")
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))??
            .parse::<u32>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        Ok(number)
    } else {
        Ok(140)
    }
}
