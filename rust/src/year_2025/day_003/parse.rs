use super::battery_bank::BatteryBank;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(sample: Option<i32>) -> Vec<BatteryBank> {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<Vec<BatteryBank>> {
    match sample {
        None => {
            let file_path = "inputs/year_2025/day_003.txt";
            let file: File = File::open(file_path)?;
            io::BufReader::new(file)
                .lines()
                .map(|line| {
                    line?
                        .parse()
                        .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))
                })
                .collect::<io::Result<Vec<BatteryBank>>>()
        }

        Some(1) => indoc! {"987654321111111
811111111111119
234234234234278
818181911112111"}
        .trim_start()
        .split("\n")
        .map(|battery_bank_raw| {
            battery_bank_raw
                .parse()
                .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))
        })
        .collect::<io::Result<Vec<BatteryBank>>>(),
        _ => Err(io::Error::new(
            io::ErrorKind::InvalidInput,
            "Sample received number is wrong",
        )),
    }
}
