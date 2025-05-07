use crate::year_2015::day_006::instruction::Instruction;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> Vec<Instruction> {
    parse_internal(from_file).expect("Error on parsing")
}

// Now implement conversion from std::io::Error to your type
pub fn parse_internal(from_file: bool) -> io::Result<Vec<Instruction>> {
    if from_file {
        let file_path = "inputs/year_2015/day_006.txt";
        let file: File = File::open(file_path)?;

        io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .into_iter()
            .map(|line| Instruction::from_raw(&line))
            .collect()
    } else {
        let hardcoded = vec![
            String::from("turn on 0,0 through 999,999"),
            String::from("toggle 0,0 through 499,499"),
            String::from("turn off 499,499 through 500,500"),
        ];
        hardcoded
            .into_iter()
            .map(|line| Instruction::from_raw(&line))
            .collect()
    }
}
