use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: u8) -> Vec<String> {
    parse_internal(from_file).expect("Error on parsing")
}

pub fn parse_internal(from_file: u8) -> io::Result<Vec<String>> {
    if from_file == 0 {
        let file_path = "inputs/year_2015/day_005.txt";
        let file: File = File::open(file_path)?;
        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()
    } else if from_file == 1 {
        let v = vec![
            String::from("ugknbfddgicrmopn"),
            String::from("aaa"),
            String::from("jchzalrnumimnmhp"),
            String::from("haegwjzuvuyypxyu"),
            String::from("dvszwmarrgswjxmb"),
        ];
        Ok(v)
    } else {
        let v = vec![
            String::from("qjhvhtzxzqqjkmpbqjhvhtzxzqqjkmpb"),
            String::from("xxyxx"),
            String::from("uurcxstgmygtbstg"),
            String::from("ieodomkazucvgmuy"),
        ];
        Ok(v)
    }
}
