use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

use super::program::Program;
use super::program_instruction::ProgramInstruction;

pub fn parse(from_file: bool) -> Program {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Program> {
    let file_path = "inputs/year_2015/day_023.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        let instructions = io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .into_iter()
            .map(|raw| ProgramInstruction::new(&raw))
            .collect::<io::Result<Vec<ProgramInstruction>>>()?;

        Ok(Program::new(instructions))
    } else {
        let program_instructions = indoc! {"inc a
                                jio a, +2
                                tpl a
                                inc a"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .map(|raw| ProgramInstruction::new(&raw))
        .collect::<io::Result<Vec<ProgramInstruction>>>()?;

        Ok(Program::new(program_instructions))
    }
}
