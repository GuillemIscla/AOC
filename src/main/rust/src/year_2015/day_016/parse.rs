use super::aunt_sue_notes::AuntSueNotes;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> Vec<AuntSueNotes> {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Vec<AuntSueNotes>> {
    let file_path = "inputs/year_2015/day_016.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .into_iter()
            .map(|raw| AuntSueNotes::from_raw(&raw))
            .collect::<io::Result<Vec<AuntSueNotes>>>()
    } else {
        indoc! {"Sue 1: goldfish: 13, trees: 4, akitas: 1
        Sue 2: goldfish: 12, trees: 0, akitas: 2"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>()
        .into_iter()
        .map(|raw| AuntSueNotes::from_raw(&raw))
        .collect::<io::Result<Vec<AuntSueNotes>>>()
    }
}
