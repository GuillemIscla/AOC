use super::homework::Homework;
use std::fs::File;
use std::io::{self, Read};

pub fn parse(sample: Option<i32>, cephalopod_reading: bool) -> Homework {
    parse_internal(sample, cephalopod_reading).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>, cephalopod_reading: bool) -> io::Result<Homework> {
    let mut content = String::new();

    match sample {
        None => {
            let file_path = "inputs/year_2025/day_006.txt";
            let mut file: File = File::open(file_path)?;
            file.read_to_string(&mut content)?;
            if content.ends_with('\n') {
                content.pop();
            }
            content = content.to_string();
        }

        Some(1) => {
            content = "123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  "
                .to_string();
        }
        _ => {
            return Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                "Sample received number is wrong",
            ))
        }
    }

    Homework::new(content, cephalopod_reading)
}
