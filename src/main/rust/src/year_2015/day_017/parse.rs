use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead, Error, ErrorKind};

pub fn parse(from_file: bool) -> Vec<usize> {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Vec<usize>> {
    let file_path = "inputs/year_2015/day_017.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .into_iter()
            .map(|raw| {
                raw.parse::<usize>()
                    .map_err(|e| Error::new(ErrorKind::InvalidData, e))
            })
            .collect::<io::Result<Vec<usize>>>()
    } else {
        indoc! {"20
        15
        10
        5
        5"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>()
        .into_iter()
        .map(|raw| {
            raw.parse::<usize>()
                .map_err(|e| Error::new(ErrorKind::InvalidData, e))
        })
        .collect::<io::Result<Vec<usize>>>()
    }
}
