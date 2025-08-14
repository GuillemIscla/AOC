use crate::year_2015::day_002::present::Present;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> Vec<Present> {
    parse_internal(from_file).expect("Error on parsing")
}

pub fn parse_internal(from_file: bool) -> io::Result<Vec<Present>> {
    let file_path = "inputs/year_2015/day_002.txt";

    let mut presents: Vec<Present> = Vec::new();
    let lines: Box<dyn Iterator<Item = String>> = if from_file {
        // Read from file
        let file = File::open(file_path)?;
        Box::new(io::BufReader::new(file).lines().map_while(Result::ok))
    } else {
        // Use hardcoded list
        let hardcoded = vec!["1x1x10".to_string()];
        Box::new(hardcoded.into_iter())
    };

    for line in lines {
        match line.split('x').collect::<Vec<_>>().as_slice() {
            [l_raw, w_raw, h_raw] => {
                if let (Ok(l), Ok(w), Ok(h)) = (
                    l_raw.parse::<u32>(),
                    w_raw.parse::<u32>(),
                    h_raw.parse::<u32>(),
                ) {
                    let present = Present::new(l, w, h);
                    presents.push(present);
                } else {
                    panic!("Error parsing dimensions as numbers");
                }
            }
            _ => panic!("Error on parsing line"),
        }
    }
    Ok(presents)
}
