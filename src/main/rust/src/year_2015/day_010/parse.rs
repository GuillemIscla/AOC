use std::fs::File;
use std::io::{self, BufRead, Error, ErrorKind};

pub fn parse(from_file: bool) -> Vec<usize> {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

pub fn switch_sample_and_parse(from_file: bool) -> io::Result<Vec<usize>> {
    let file_path = "inputs/year_2015/day_010.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        let number_list_from_file = io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?
            .first()
            .ok_or_else(|| Error::new(ErrorKind::NotFound, "Value was not present"))?
            .chars()
            .map(|c| {
                c.to_digit(10)
                    .map(|d| d as usize)
                    .ok_or_else(|| Error::new(ErrorKind::NotFound, "Not a valid digit"))
            })
            .collect::<io::Result<Vec<usize>>>()?;

        Ok(number_list_from_file)
    } else {
        Ok(vec![1])
    }
}
