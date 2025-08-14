use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead, Error, ErrorKind};

pub fn parse(from_file: bool) -> Vec<usize> {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Vec<usize>> {
    let file_path = "inputs/year_2015/day_024.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        let packages = io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .into_iter()
            .map(|raw| {
                raw.parse::<usize>()
                    .map_err(|e| Error::new(ErrorKind::InvalidInput, e))
            })
            .collect::<io::Result<Vec<usize>>>()?;

        Ok(packages)
    } else {
        let packages = indoc! {"1
                2
                3
                4
                5
                7
                8
                9
                10
                11"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .map(|raw| {
            raw.parse::<usize>()
                .map_err(|e| Error::new(ErrorKind::InvalidInput, e))
        })
        .collect::<io::Result<Vec<usize>>>()?;

        Ok(packages)
    }
}
