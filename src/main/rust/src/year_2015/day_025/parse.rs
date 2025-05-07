use indoc::indoc;
use regex::Regex;
use std::fs::File;
use std::io::{self, BufRead, Error, ErrorKind};

pub fn parse(from_file: bool) -> (usize, usize) {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<(usize, usize)> {
    let file_path = "inputs/year_2015/day_025.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        let to_continue_raw = io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .first()
            .ok_or(Error::new(
                ErrorKind::InvalidInput,
                "Cannot find the first line",
            ))?
            .clone();

        let regex_raw = r".*row ([0-9]+), column ([0-9]+)";
        let regex = Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures = regex.captures(&to_continue_raw).ok_or_else(|| {
            Error::new(
                ErrorKind::InvalidData,
                format!("Invalid line: {}", to_continue_raw),
            )
        })?;

        let row = captures[1]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        let column = captures[2]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        Ok((row, column))
    } else {
        let to_continue_raw = indoc! {"To continue, please consult the code grid in the manual.  Enter the code at row 2, column 3."}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>()
            .first()
            .ok_or(Error::new(
                ErrorKind::InvalidInput,
                "Cannot find the first line",
            ))?.clone();

        let regex_raw = r".*row ([0-9]+), column ([0-9]+)";
        let regex = Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures = regex.captures(&to_continue_raw).ok_or_else(|| {
            Error::new(
                ErrorKind::InvalidData,
                format!("Invalid line: {}", to_continue_raw),
            )
        })?;

        let row = captures[1]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        let column = captures[2]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        Ok((row, column))
    }
}
