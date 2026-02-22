use crate::current_day::rotation::Rotation;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(sample: Option<i32>) -> Vec<Rotation> {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<Vec<Rotation>> {
    match sample {
        None => {
            let file_path = "inputs/year_2025/day_001.txt";
            let file: File = File::open(file_path)?;
            io::BufReader::new(file)
                .lines()
                .map(|line| {
                    line?
                        .parse()
                        .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))
                })
                .collect::<io::Result<Vec<Rotation>>>()
        }

        Some(1) => indoc! {"L68
                     L30
                     R48
                     L5
                     R60
                     L55
                     L1
                     L99
                     R14
                     L82"}
        .trim_start()
        .split("\n")
        .map(|line| {
            line.parse()
                .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))
        })
        .collect::<io::Result<Vec<Rotation>>>(),
        Some(2) => indoc! {"R1000"}
            .trim_start()
            .split("\n")
            .map(|line| {
                line.parse()
                    .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))
            })
            .collect::<io::Result<Vec<Rotation>>>(),
        _ => Err(io::Error::new(
            io::ErrorKind::InvalidInput,
            "Sample received number is wrong",
        )),
    }
}
