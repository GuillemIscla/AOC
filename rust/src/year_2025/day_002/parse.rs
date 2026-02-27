use super::range::Range;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(sample: Option<i32>) -> Vec<Range> {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<Vec<Range>> {
    match sample {
        None => {
            let file_path = "inputs/year_2025/day_002.txt";
            let file: File = File::open(file_path)?;
            let first_line: String = io::BufReader::new(file)
                .lines()
                .next()
                .ok_or_else(|| io::Error::new(io::ErrorKind::UnexpectedEof, "File is empty"))??;

            first_line
                .split(",")
                .map(|range_raw| {
                    range_raw
                        .parse()
                        .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))
                })
                .collect::<io::Result<Vec<Range>>>()
        }

        Some(1) => indoc! {"11-22,95-115,998-1012,1188511880-1188511890,
        222220-222224,1698522-1698528,446443-446449,
        38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"}
        .replace('\n', "")
        .trim_start()
        .split(",")
        .map(|range_raw| {
            range_raw
                .parse()
                .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))
        })
        .collect::<io::Result<Vec<Range>>>(),
        _ => Err(io::Error::new(
            io::ErrorKind::InvalidInput,
            "Sample received number is wrong",
        )),
    }
}
