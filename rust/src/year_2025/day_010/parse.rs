use crate::year_2025::day_010::factory_machine::FactoryMachine;
use std::fs::File;
use std::io::{self, Read};

pub fn parse(sample: Option<i32>) -> Vec<FactoryMachine> {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<Vec<FactoryMachine>> {
    let mut content = String::new();

    match sample {
        None => {
            let file_path = "inputs/year_2025/day_010.txt";
            let mut file: File = File::open(file_path)?;
            file.read_to_string(&mut content)?;
            if content.ends_with('\n') {
                content.pop();
            }
            content = content.to_string();
        }

        Some(1) => {
            content = "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
                .to_string();
        }
        _ => {
            return Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                "Sample received number is wrong",
            ))
        }
    }

    let tiles = content
        .lines()
        .map(FactoryMachine::new)
        .collect::<io::Result<Vec<FactoryMachine>>>()?;

    Ok(tiles)
}
