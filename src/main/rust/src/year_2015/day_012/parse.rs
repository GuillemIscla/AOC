use std::fs::File;
use std::io::{self, BufRead, Error, ErrorKind};

pub fn parse(from_file: Option<usize>) -> String {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

pub fn switch_sample_and_parse(from_file: Option<usize>) -> io::Result<String> {
    let file_path = "inputs/year_2015/day_012.txt";
    let file: File = File::open(file_path)?;

    match from_file {
        None => io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .first()
            .ok_or_else(|| Error::new(ErrorKind::NotFound, "Value was not present"))
            .cloned(),
        Some(0) => Ok(String::from("[1,2,3]")),
        Some(1) => Ok(String::from("{\"a\":2,\"b\":4}")),
        Some(2) => Ok(String::from("[[[3]]]")),
        Some(3) => Ok(String::from("{\"a\":{\"b\":4},\"c\":-1}")),
        Some(4) => Ok(String::from("{\"a\":[-1,1]}")),
        Some(5) => Ok(String::from("[-1,{\"a\":1}]")),
        Some(6) => Ok(String::from("[]")),
        Some(7) => Ok(String::from("{}")),
        Some(8) => Ok(String::from("[1,{\"c\":\"red\",\"b\":2},3]")),
        Some(9) => Ok(String::from("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}")),
        _ => Ok(String::from("[1,\"red\",5]")),
    }
}
