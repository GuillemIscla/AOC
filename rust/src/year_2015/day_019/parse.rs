use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead, Error, ErrorKind};

use super::laboratory::Laboratory;

pub fn parse(from_file: bool) -> Laboratory {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Laboratory> {
    let file_path = "inputs/year_2015/day_019.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        let lines = io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?;

        let initial_molecule = lines
            .iter()
            .last()
            .ok_or("empty")
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?
            .to_string();

        let replacements_raw = lines
            .into_iter()
            .take_while(|line| !line.is_empty())
            .collect::<Vec<String>>();

        Ok(Laboratory::new(replacements_raw, initial_molecule))
    } else {
        let lines = indoc! {"H => HO
        H => OH
        O => HH
        e => H
        e => O

        HOH"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>();

        let initial_molecule = lines
            .iter()
            .last()
            .ok_or("empty")
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?
            .to_string();

        let replacements_raw = lines
            .into_iter()
            .take_while(|line| !line.is_empty())
            .collect::<Vec<String>>();

        Ok(Laboratory::new(replacements_raw, initial_molecule))
    }
}
